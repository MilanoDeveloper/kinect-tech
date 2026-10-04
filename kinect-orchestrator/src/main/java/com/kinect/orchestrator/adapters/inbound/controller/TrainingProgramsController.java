package com.kinect.orchestrator.adapters.inbound.controller;

import com.kinect.contracts.orchestrator.api.TrainingProgramsApi;
import com.kinect.contracts.trainingprograms.dto.TrainingProgram;
import com.kinect.contracts.trainingprograms.dto.TrainingProgramRequest;
import com.kinect.orchestrator.application.port.in.GymOperationsUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TrainingProgramsController implements TrainingProgramsApi {
    private final GymOperationsUseCase useCase;

    public TrainingProgramsController(GymOperationsUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public ResponseEntity<Void> createTrainingProgram(TrainingProgramRequest request) {
        useCase.createTrainingProgram(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<List<TrainingProgram>> getTrainingPrograms() {
        return ResponseEntity.ok(useCase.findTrainingPrograms());
    }

    @Override
    public ResponseEntity<TrainingProgram> getTrainingProgramById(Long trainingProgramId) {
        return ResponseEntity.ok(useCase.findTrainingProgram(trainingProgramId));
    }

    @Override
    public ResponseEntity<Void> updateTrainingProgram(Long trainingProgramId, TrainingProgramRequest request) {
        useCase.updateTrainingProgram(trainingProgramId, request);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteTrainingProgram(Long trainingProgramId) {
        useCase.deleteTrainingProgram(trainingProgramId);
        return ResponseEntity.noContent().build();
    }
}
