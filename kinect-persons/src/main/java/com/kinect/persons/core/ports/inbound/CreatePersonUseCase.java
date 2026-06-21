package com.kinect.persons.core.ports.inbound;

import com.kinect.persons.core.domain.Person;

public interface CreatePersonUseCase {
    void execute(Person person);
}