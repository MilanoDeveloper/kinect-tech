package com.kinect.persons.core.ports.outbound;

import com.kinect.persons.core.domain.Person;

import java.util.List;
import java.util.Optional;

public interface PersonOutputPort {
    Person save(Person person);
    List<Person> findAll();
    Optional<Person> findById(Long id);
    boolean deleteById(Long id);
    boolean existsByUsername(String username);
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
    boolean existsByUsernameAndIdNot(String username, Long id);
    boolean existsByCpfAndIdNot(String cpf, Long id);
    boolean existsByEmailAndIdNot(String email, Long id);
}