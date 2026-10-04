package com.kinect.persons.adapters.outbound.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "persons", schema = "persons")
public class PersonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false, length = 1)
    private String gender;

    @Column(name = "person_type", nullable = false, length = 20)
    private String personType;

    @Column(name = "internal_personal")
    private Boolean internalPersonal;

    @Column(length = 9, unique = true)
    private String cref;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(nullable = false, unique = true)
    private String email;

    private String note;

    private Double height;

    private Double weight;

    @Column(name = "body_fat_percentage")
    private Double bodyFatPercentage;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "person_medical_conditions", schema = "persons",
            joinColumns = @JoinColumn(name = "person_id"))
    @Column(name = "medical_condition", nullable = false)
    private List<String> medicalConditions = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDate updatedAt;

}