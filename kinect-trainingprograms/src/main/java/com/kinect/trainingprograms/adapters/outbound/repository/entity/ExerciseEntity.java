package com.kinect.trainingprograms.adapters.outbound.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ExerciseEntity {
    @Column(name = "exercise_name", nullable = false, length = 120)
    private String name;

    @Column(name = "sets_count")
    private Integer sets;

    private Integer repetitions;

    @Column(name = "rest_seconds")
    private Integer restSeconds;

    @Column(length = 500)
    private String notes;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getSets() { return sets; }
    public void setSets(Integer sets) { this.sets = sets; }
    public Integer getRepetitions() { return repetitions; }
    public void setRepetitions(Integer repetitions) { this.repetitions = repetitions; }
    public Integer getRestSeconds() { return restSeconds; }
    public void setRestSeconds(Integer restSeconds) { this.restSeconds = restSeconds; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
