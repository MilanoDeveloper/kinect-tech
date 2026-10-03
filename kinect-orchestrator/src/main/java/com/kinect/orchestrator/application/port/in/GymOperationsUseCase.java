package com.kinect.orchestrator.application.port.in;

import com.kinect.contracts.persons.dto.CreatePersonRequest;
import com.kinect.contracts.persons.dto.GetPersonsApi;
import com.kinect.contracts.payments.dto.Payment;
import com.kinect.contracts.payments.dto.PaymentRequest;
import com.kinect.contracts.trainingprograms.dto.TrainingProgram;
import com.kinect.contracts.trainingprograms.dto.TrainingProgramRequest;

import java.util.List;

public interface GymOperationsUseCase {
    void createPerson(CreatePersonRequest request);
    List<GetPersonsApi> findPersons();
    GetPersonsApi findPerson(Long personId);
    void updatePerson(Long personId, CreatePersonRequest request);
    void deletePerson(Long personId);
    void createPayment(PaymentRequest request);
    List<Payment> findPayments();
    Payment findPayment(Long paymentId);
    void updatePayment(Long paymentId, PaymentRequest request);
    void deletePayment(Long paymentId);
    void createTrainingProgram(TrainingProgramRequest request);
    List<TrainingProgram> findTrainingPrograms();
    TrainingProgram findTrainingProgram(Long trainingProgramId);
    void updateTrainingProgram(Long trainingProgramId, TrainingProgramRequest request);
    void deleteTrainingProgram(Long trainingProgramId);
}
