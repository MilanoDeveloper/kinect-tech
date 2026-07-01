package com.kinect.persons.core.ports.outbound;

import com.kinect.persons.core.domain.Person;

import java.util.List;

public interface PersonOutputPort {
    Person save(Person person);
    List<Person> findAll();
    boolean existsByUsername(String username);
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
}