package com.kinect.trainingprograms.adapters.outbound.repository;

import com.kinect.trainingprograms.adapters.outbound.repository.entity.TrainingProgramEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingProgramJpaRepository extends JpaRepository<TrainingProgramEntity, Long> {
}
