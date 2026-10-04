package com.kinect.persons.core.ports.inbound;

import com.kinect.persons.core.domain.Person;

import java.util.Optional;

public interface ManagePersonsUseCase {
    Optional<Person> findById(Long id);
    boolean update(Long id, Person person);
    boolean delete(Long id);
}
