package com.kinect.orchestrator.adapters.outbound.http;

import com.kinect.contracts.persons.dto.CreatePersonRequest;
import com.kinect.contracts.persons.dto.GetPersonsApi;
import com.kinect.contracts.payments.dto.Payment;
import com.kinect.contracts.payments.dto.PaymentRequest;
import com.kinect.contracts.trainingprograms.dto.TrainingProgram;
import com.kinect.contracts.trainingprograms.dto.TrainingProgramRequest;
import com.kinect.orchestrator.application.exception.DownstreamServiceException;
import com.kinect.orchestrator.application.port.out.PaymentsServicePort;
import com.kinect.orchestrator.application.port.out.PersonsServicePort;
import com.kinect.orchestrator.application.port.out.TrainingProgramsServicePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class MicroservicesHttpAdapter
        implements PersonsServicePort, PaymentsServicePort, TrainingProgramsServicePort {
    private static final ParameterizedTypeReference<List<GetPersonsApi>> PERSONS_LIST =
            new ParameterizedTypeReference<>() { };
    private static final ParameterizedTypeReference<List<Payment>> PAYMENTS_LIST =
            new ParameterizedTypeReference<>() { };
    private static final ParameterizedTypeReference<List<TrainingProgram>> TRAINING_PROGRAMS_LIST =
            new ParameterizedTypeReference<>() { };

    private final RestClient restClient = RestClient.create();
    private final String personsUrl;
    private final String paymentsUrl;
    private final String trainingProgramsUrl;

    public MicroservicesHttpAdapter(
            @Value("${kinect.services.persons-url}") String personsUrl,
            @Value("${kinect.services.payments-url}") String paymentsUrl,
            @Value("${kinect.services.training-programs-url}") String trainingProgramsUrl) {
        this.personsUrl = normalizeUrl(personsUrl);
        this.paymentsUrl = normalizeUrl(paymentsUrl);
        this.trainingProgramsUrl = normalizeUrl(trainingProgramsUrl);
    }

    @Override
    public void createPerson(CreatePersonRequest request) {
        post(personsUrl, "/api/v1/persons", request);
    }

    @Override
    public List<GetPersonsApi> findAllPersons() {
        return getList(personsUrl, "/api/v1/persons", PERSONS_LIST);
    }

    @Override
    public GetPersonsApi findPersonById(Long personId) {
        return getOrNull(personsUrl, "/api/v1/persons/" + personId, GetPersonsApi.class);
    }

    @Override
    public void updatePerson(Long personId, CreatePersonRequest request) {
        put(personsUrl, "/api/v1/persons/" + personId, request);
    }

    @Override
    public void deletePerson(Long personId) {
        delete(personsUrl, "/api/v1/persons/" + personId);
    }

    @Override
    public void createPayment(PaymentRequest request) {
        post(paymentsUrl, "/api/v1/payments", request);
    }

    @Override
    public List<Payment> findAllPayments() {
        return getList(paymentsUrl, "/api/v1/payments", PAYMENTS_LIST);
    }

    @Override
    public Payment findPaymentById(Long paymentId) {
        return getOrNull(paymentsUrl, "/api/v1/payments/" + paymentId, Payment.class);
    }

    @Override
    public void updatePayment(Long paymentId, PaymentRequest request) {
        put(paymentsUrl, "/api/v1/payments/" + paymentId, request);
    }

    @Override
    public void deletePayment(Long paymentId) {
        delete(paymentsUrl, "/api/v1/payments/" + paymentId);
    }

    @Override
    public void createTrainingProgram(TrainingProgramRequest request) {
        post(trainingProgramsUrl, "/api/v1/training-programs", request);
    }

    @Override
    public List<TrainingProgram> findAllTrainingPrograms() {
        return getList(trainingProgramsUrl, "/api/v1/training-programs", TRAINING_PROGRAMS_LIST);
    }

    @Override
    public TrainingProgram findTrainingProgramById(Long trainingProgramId) {
        return getOrNull(trainingProgramsUrl, "/api/v1/training-programs/" + trainingProgramId,
                TrainingProgram.class);
    }

    @Override
    public void updateTrainingProgram(Long trainingProgramId, TrainingProgramRequest request) {
        put(trainingProgramsUrl, "/api/v1/training-programs/" + trainingProgramId, request);
    }

    @Override
    public void deleteTrainingProgram(Long trainingProgramId) {
        delete(trainingProgramsUrl, "/api/v1/training-programs/" + trainingProgramId);
    }

    private <T> T get(String baseUrl, String path, Class<T> responseType) {
        return restClient.get().uri(baseUrl + path)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw downstreamError(response.getStatusCode(), path);
                })
                .body(responseType);
    }

    private <T> T getList(
            String baseUrl, String path, ParameterizedTypeReference<T> responseType) {
        return restClient.get().uri(baseUrl + path)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw downstreamError(response.getStatusCode(), path);
                })
                .body(responseType);
    }

    private <T> T getOrNull(String baseUrl, String path, Class<T> responseType) {
        try {
            return get(baseUrl, path, responseType);
        } catch (DownstreamServiceException exception) {
            if (exception.getDownstreamStatus() == 404) {
                return null;
            }
            throw exception;
        }
    }

    private void post(String baseUrl, String path, Object body) {
        restClient.post().uri(baseUrl + path).body(body)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw downstreamError(response.getStatusCode(), path);
                })
                .toBodilessEntity();
    }

    private void put(String baseUrl, String path, Object body) {
        restClient.put().uri(baseUrl + path).body(body)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw downstreamError(response.getStatusCode(), path);
                })
                .toBodilessEntity();
    }

    private void delete(String baseUrl, String path) {
        restClient.delete().uri(baseUrl + path)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw downstreamError(response.getStatusCode(), path);
                })
                .toBodilessEntity();
    }

    private DownstreamServiceException downstreamError(HttpStatusCode status, String path) {
        return new DownstreamServiceException(status.value(),
                "Downstream request " + path + " returned HTTP " + status.value());
    }

    private String normalizeUrl(String url) {
        return url.replaceAll("/+$", "");
    }
}
