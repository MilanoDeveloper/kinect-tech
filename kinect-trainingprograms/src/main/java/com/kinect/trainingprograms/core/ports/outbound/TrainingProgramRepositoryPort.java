package com.kinect.trainingprograms.core.ports.outbound;

import com.kinect.trainingprograms.core.domain.TrainingProgram;

import java.util.List;
import java.util.Optional;

public interface TrainingProgramRepositoryPort {
    TrainingProgram save(TrainingProgram program);
    List<TrainingProgram> findAll();
    Optional<TrainingProgram> findById(Long trainingProgramId);
    boolean deleteById(Long trainingProgramId);
}
