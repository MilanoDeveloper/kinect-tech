package com.kinect.persons.core.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class Person {

    private Long id;
    private String username;
    private String password;
    private String name;
    private LocalDate birthDate;
    private String gender;
    private String personType;
    private Boolean internalPersonal;
    private String cref;
    private String cpf;
    private String email;
    private String note;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
