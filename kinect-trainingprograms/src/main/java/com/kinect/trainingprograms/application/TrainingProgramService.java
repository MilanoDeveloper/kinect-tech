package com.kinect.trainingprograms.application;

import com.kinect.trainingprograms.core.domain.TrainingProgram;
import com.kinect.trainingprograms.core.ports.inbound.TrainingProgramUseCase;
import com.kinect.trainingprograms.core.ports.outbound.TrainingProgramRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainingProgramService implements TrainingProgramUseCase {
    private final TrainingProgramRepositoryPort repository;

    public TrainingProgramService(TrainingProgramRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public TrainingProgram create(TrainingProgram program) {
        return repository.save(program);
    }

    @Override
    public List<TrainingProgram> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<TrainingProgram> findById(Long trainingProgramId) {
        return repository.findById(trainingProgramId);
    }

    @Override
    public Optional<TrainingProgram> update(Long trainingProgramId, TrainingProgram program) {
        if (repository.findById(trainingProgramId).isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(repository.save(new TrainingProgram(
                trainingProgramId, program.studentId(), program.trainerId(), program.name(),
                program.exercises(), null, null)));
    }

    @Override
    public boolean delete(Long trainingProgramId) {
        return repository.deleteById(trainingProgramId);
    }
}
