package com.kinect.orchestrator.application.port.out;

import com.kinect.contracts.trainingprograms.dto.TrainingProgram;
import com.kinect.contracts.trainingprograms.dto.TrainingProgramRequest;

import java.util.List;

public interface TrainingProgramsServicePort {
    void createTrainingProgram(TrainingProgramRequest request);
    List<TrainingProgram> findAllTrainingPrograms();
    TrainingProgram findTrainingProgramById(Long trainingProgramId);
    void updateTrainingProgram(Long trainingProgramId, TrainingProgramRequest request);
    void deleteTrainingProgram(Long trainingProgramId);
}
