package com.kinect.trainingprograms.core.domain;

import java.time.LocalDate;
import java.util.List;

public record TrainingProgram(
        Long id,
        Long studentId,
        Long trainerId,
        String name,
        List<Exercise> exercises,
        LocalDate createdAt,
        LocalDate updatedAt) {
}
