package com.kinect.persons.core.ports.outbound;

import com.kinect.persons.core.domain.Person;

public interface PersonOutputPort {
    Person save(Person person);
    boolean existsByUsername(String username);
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
}