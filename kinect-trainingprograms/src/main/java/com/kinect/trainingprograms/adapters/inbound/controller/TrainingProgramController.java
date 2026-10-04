package com.kinect.trainingprograms.adapters.inbound.controller;

import com.kinect.contracts.trainingprograms.api.TrainingProgramsApi;
import com.kinect.contracts.trainingprograms.dto.Exercise;
import com.kinect.contracts.trainingprograms.dto.TrainingProgram;
import com.kinect.contracts.trainingprograms.dto.TrainingProgramRequest;
import com.kinect.trainingprograms.core.ports.inbound.TrainingProgramUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
public class TrainingProgramController implements TrainingProgramsApi {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private final TrainingProgramUseCase useCase;

    public TrainingProgramController(TrainingProgramUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public ResponseEntity<Void> createTrainingProgram(TrainingProgramRequest request) {
        useCase.create(toDomain(request, null));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<List<TrainingProgram>> getTrainingPrograms() {
        return ResponseEntity.ok(useCase.findAll().stream().map(this::toResponse).toList());
    }

    @Override
    public ResponseEntity<TrainingProgram> getTrainingProgramById(Long trainingProgramId) {
        return ResponseEntity.ok(useCase.findById(trainingProgramId)
                .map(this::toResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Training program not found")));
    }

    @Override
    public ResponseEntity<Void> updateTrainingProgram(Long trainingProgramId, TrainingProgramRequest request) {
        if (useCase.update(trainingProgramId, toDomain(request, trainingProgramId)).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Training program not found");
        }
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteTrainingProgram(Long trainingProgramId) {
        if (!useCase.delete(trainingProgramId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Training program not found");
        }
        return ResponseEntity.noContent().build();
    }

    private com.kinect.trainingprograms.core.domain.TrainingProgram toDomain(
            TrainingProgramRequest request, Long id) {
        List<com.kinect.trainingprograms.core.domain.Exercise> exercises = request.getExercises().stream()
                .map(exercise -> new com.kinect.trainingprograms.core.domain.Exercise(
                        exercise.getName(), exercise.getSets(), exercise.getRepetitions(),
                        exercise.getRestSeconds(), exercise.getNotes()))
                .toList();
        return new com.kinect.trainingprograms.core.domain.TrainingProgram(
                id, request.getStudentId(), request.getTrainerId(), request.getName(), exercises, null, null);
    }

    private TrainingProgram toResponse(com.kinect.trainingprograms.core.domain.TrainingProgram program) {
        List<Exercise> exercises = program.exercises().stream()
                .map(exercise -> {
                    Exercise response = new Exercise();
                    response.setName(exercise.name());
                    response.setSets(exercise.sets());
                    response.setRepetitions(exercise.repetitions());
                    response.setRestSeconds(exercise.restSeconds());
                    response.setNotes(exercise.notes());
                    return response;
                })
                .toList();
        TrainingProgram response = new TrainingProgram();
        response.setId(program.id());
        response.setStudentId(program.studentId());
        response.setTrainerId(program.trainerId());
        response.setName(program.name());
        response.setExercises(exercises);
        response.setCreatedAt(formatDate(program.createdAt()));
        response.setUpdatedAt(formatDate(program.updatedAt()));
        return response;
    }

    private String formatDate(LocalDate date) {
        return date == null ? null : date.format(DATE_FORMAT);
    }
}
