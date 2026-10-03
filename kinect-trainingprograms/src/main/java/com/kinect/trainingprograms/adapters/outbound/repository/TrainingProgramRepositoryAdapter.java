package com.kinect.trainingprograms.adapters.outbound.repository;

import com.kinect.trainingprograms.adapters.outbound.repository.entity.ExerciseEntity;
import com.kinect.trainingprograms.adapters.outbound.repository.entity.TrainingProgramEntity;
import com.kinect.trainingprograms.core.domain.Exercise;
import com.kinect.trainingprograms.core.domain.TrainingProgram;
import com.kinect.trainingprograms.core.ports.outbound.TrainingProgramRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TrainingProgramRepositoryAdapter implements TrainingProgramRepositoryPort {
    private final TrainingProgramJpaRepository repository;

    public TrainingProgramRepositoryAdapter(TrainingProgramJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public TrainingProgram save(TrainingProgram program) {
        TrainingProgramEntity entity = program.id() == null
                ? new TrainingProgramEntity()
                : repository.findById(program.id()).orElseThrow();
        entity.setStudentId(program.studentId());
        entity.setTrainerId(program.trainerId());
        entity.setName(program.name());
        entity.getExercises().clear();
        entity.getExercises().addAll(program.exercises().stream().map(this::toEntity).toList());
        return toDomain(repository.save(entity));
    }

    @Override
    public List<TrainingProgram> findAll() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<TrainingProgram> findById(Long trainingProgramId) {
        return repository.findById(trainingProgramId).map(this::toDomain);
    }

    @Override
    public boolean deleteById(Long trainingProgramId) {
        if (!repository.existsById(trainingProgramId)) {
            return false;
        }
        repository.deleteById(trainingProgramId);
        return true;
    }

    private ExerciseEntity toEntity(Exercise exercise) {
        ExerciseEntity entity = new ExerciseEntity();
        entity.setName(exercise.name());
        entity.setSets(exercise.sets());
        entity.setRepetitions(exercise.repetitions());
        entity.setRestSeconds(exercise.restSeconds());
        entity.setNotes(exercise.notes());
        return entity;
    }

    private TrainingProgram toDomain(TrainingProgramEntity entity) {
        List<Exercise> exercises = entity.getExercises().stream()
                .map(exercise -> new Exercise(exercise.getName(), exercise.getSets(),
                        exercise.getRepetitions(), exercise.getRestSeconds(), exercise.getNotes()))
                .toList();
        return new TrainingProgram(entity.getId(), entity.getStudentId(), entity.getTrainerId(),
                entity.getName(), exercises, entity.getCreatedAt(), entity.getUpdatedAt());
    }
}
