package com.kinect.persons.application.services;

import com.kinect.persons.core.domain.Person;
import com.kinect.persons.core.ports.inbound.CreatePersonUseCase;
import com.kinect.persons.core.ports.inbound.ManagePersonsUseCase;
import com.kinect.persons.core.ports.outbound.PersonOutputPort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CreatePersonService implements CreatePersonUseCase, ManagePersonsUseCase {

    private final PersonOutputPort personOutputPort;

    public CreatePersonService(PersonOutputPort personOutputPort) {
        this.personOutputPort = personOutputPort;
    }

    @Override
    public void execute(Person person) {
        ensureUniqueValues(person, null);
        personOutputPort.save(person);
    }

    @Override
    public Optional<Person> findById(Long id) {
        return personOutputPort.findById(id);
    }

    @Override
    public boolean update(Long id, Person person) {
        if (personOutputPort.findById(id).isEmpty()) {
            return false;
        }
        ensureUniqueValues(person, id);
        person.setId(id);
        personOutputPort.save(person);
        return true;
    }

    @Override
    public boolean delete(Long id) {
        return personOutputPort.deleteById(id);
    }

    private void ensureUniqueValues(Person person, Long excludedId) {
        if (excludedId == null) {
            if (personOutputPort.existsByUsername(person.getUsername())) {
                throw new IllegalArgumentException("Username already exists");
            }
            if (personOutputPort.existsByCpf(person.getCpf())) {
                throw new IllegalArgumentException("CPF already registered");
            }
            if (personOutputPort.existsByEmail(person.getEmail())) {
                throw new IllegalArgumentException("Email already registered");
            }
            return;
        }
        if (personOutputPort.existsByUsernameAndIdNot(person.getUsername(), excludedId)) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (personOutputPort.existsByCpfAndIdNot(person.getCpf(), excludedId)) {
            throw new IllegalArgumentException("CPF already registered");
        }
        if (personOutputPort.existsByEmailAndIdNot(person.getEmail(), excludedId)) {
            throw new IllegalArgumentException("Email already registered");
        }
    }
}