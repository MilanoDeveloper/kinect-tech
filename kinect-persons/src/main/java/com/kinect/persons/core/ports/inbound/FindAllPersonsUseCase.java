package com.kinect.persons.core.ports.inbound;

import com.kinect.persons.core.domain.Person;

import java.util.List;

public interface FindAllPersonsUseCase {
    List<Person> execute();
}
