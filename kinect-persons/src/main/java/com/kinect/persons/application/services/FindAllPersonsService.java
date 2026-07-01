package com.kinect.persons.application.services;


import com.kinect.persons.core.domain.Person;
import com.kinect.persons.core.ports.inbound.FindAllPersonsUseCase;
import com.kinect.persons.core.ports.outbound.PersonOutputPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindAllPersonsService implements FindAllPersonsUseCase {

    private final PersonOutputPort personOutputPort;

    public FindAllPersonsService(PersonOutputPort personOutputPort) {
        this.personOutputPort = personOutputPort;
    }

    @Override
    public List<Person> execute() {
        return personOutputPort.findAll();
    }
}
