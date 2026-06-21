package com.kinect.persons.application.services;

import com.kinect.persons.core.domain.Person;
import com.kinect.persons.core.ports.inbound.CreatePersonUseCase;
import com.kinect.persons.core.ports.outbound.PersonOutputPort;
import org.springframework.stereotype.Service;

@Service
public class CreatePersonService implements CreatePersonUseCase {

    private final PersonOutputPort personOutputPort;

    public CreatePersonService(PersonOutputPort personOutputPort) {
        this.personOutputPort = personOutputPort;
    }

    @Override
    public void execute(Person person) {
        // Validações de Regra de Negócio (Domínio)
        if (personOutputPort.existsByUsername(person.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (personOutputPort.existsByCpf(person.getCpf())) {
            throw new IllegalArgumentException("CPF already registered");
        }
        if (personOutputPort.existsByEmail(person.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        // Aqui você aplicaria criptografia na senha (ex: BCryptEncoder) antes de salvar

        personOutputPort.save(person);
    }
}