package com.kinect.trainingprograms.core.ports.inbound;

import com.kinect.trainingprograms.core.domain.TrainingProgram;

import java.util.List;
import java.util.Optional;

public interface TrainingProgramUseCase {
    TrainingProgram create(TrainingProgram program);
    List<TrainingProgram> findAll();
    Optional<TrainingProgram> findById(Long trainingProgramId);
    Optional<TrainingProgram> update(Long trainingProgramId, TrainingProgram program);
    boolean delete(Long trainingProgramId);
}
