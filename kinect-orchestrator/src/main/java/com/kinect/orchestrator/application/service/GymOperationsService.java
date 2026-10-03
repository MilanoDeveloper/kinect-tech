package com.kinect.orchestrator.application.service;

import com.kinect.contracts.persons.dto.CreatePersonRequest;
import com.kinect.contracts.persons.dto.GetPersonsApi;
import com.kinect.contracts.payments.dto.Payment;
import com.kinect.contracts.payments.dto.PaymentRequest;
import com.kinect.contracts.trainingprograms.dto.TrainingProgram;
import com.kinect.contracts.trainingprograms.dto.TrainingProgramRequest;
import com.kinect.orchestrator.application.exception.ResourceNotFoundException;
import com.kinect.orchestrator.application.port.in.GymOperationsUseCase;
import com.kinect.orchestrator.application.port.out.PaymentsServicePort;
import com.kinect.orchestrator.application.port.out.PersonsServicePort;
import com.kinect.orchestrator.application.port.out.TrainingProgramsServicePort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GymOperationsService implements GymOperationsUseCase {
    private final PersonsServicePort persons;
    private final PaymentsServicePort payments;
    private final TrainingProgramsServicePort trainingPrograms;

    public GymOperationsService(
            PersonsServicePort persons,
            PaymentsServicePort payments,
            TrainingProgramsServicePort trainingPrograms) {
        this.persons = persons;
        this.payments = payments;
        this.trainingPrograms = trainingPrograms;
    }

    @Override
    public void createPerson(CreatePersonRequest request) {
        persons.createPerson(request);
    }

    @Override
    public List<GetPersonsApi> findPersons() {
        return persons.findAllPersons();
    }

    @Override
    public GetPersonsApi findPerson(Long personId) {
        return requirePerson(personId);
    }

    @Override
    public void updatePerson(Long personId, CreatePersonRequest request) {
        persons.updatePerson(personId, request);
    }

    @Override
    public void deletePerson(Long personId) {
        persons.deletePerson(personId);
    }

    @Override
    public void createPayment(PaymentRequest request) {
        requirePerson(request.getPersonId());
        payments.createPayment(request);
    }

    @Override
    public List<Payment> findPayments() {
        return payments.findAllPayments();
    }

    @Override
    public Payment findPayment(Long paymentId) {
        Payment payment = payments.findPaymentById(paymentId);
        if (payment == null) {
            throw new ResourceNotFoundException("Payment " + paymentId + " not found");
        }
        return payment;
    }

    @Override
    public void updatePayment(Long paymentId, PaymentRequest request) {
        requirePerson(request.getPersonId());
        payments.updatePayment(paymentId, request);
    }

    @Override
    public void deletePayment(Long paymentId) {
        payments.deletePayment(paymentId);
    }

    @Override
    public void createTrainingProgram(TrainingProgramRequest request) {
        requirePerson(request.getStudentId());
        trainingPrograms.createTrainingProgram(request);
    }

    @Override
    public List<TrainingProgram> findTrainingPrograms() {
        return trainingPrograms.findAllTrainingPrograms();
    }

    @Override
    public TrainingProgram findTrainingProgram(Long trainingProgramId) {
        TrainingProgram program = trainingPrograms.findTrainingProgramById(trainingProgramId);
        if (program == null) {
            throw new ResourceNotFoundException("Training program " + trainingProgramId + " not found");
        }
        return program;
    }

    @Override
    public void updateTrainingProgram(Long trainingProgramId, TrainingProgramRequest request) {
        requirePerson(request.getStudentId());
        trainingPrograms.updateTrainingProgram(trainingProgramId, request);
    }

    @Override
    public void deleteTrainingProgram(Long trainingProgramId) {
        trainingPrograms.deleteTrainingProgram(trainingProgramId);
    }

    private GetPersonsApi requirePerson(Long personId) {
        GetPersonsApi person = persons.findPersonById(personId);
        if (person == null) {
            throw new ResourceNotFoundException("Person " + personId + " not found");
        }
        return person;
    }
}
