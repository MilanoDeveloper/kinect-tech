package com.kinect.persons.adapters.inbound.controller;

import com.kinect.contracts.persons.api.PersonsApi;
import com.kinect.contracts.persons.dto.CreatePersonRequest;
import com.kinect.persons.adapters.mapper.PersonMapper;
import com.kinect.persons.core.domain.Person;
import com.kinect.persons.core.ports.inbound.CreatePersonUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class PersonController implements PersonsApi {

    private final CreatePersonUseCase createPersonUseCase;
    private final PersonMapper personMapper;

    public PersonController(CreatePersonUseCase createPersonUseCase, PersonMapper personMapper) {
        this.createPersonUseCase = createPersonUseCase;
        this.personMapper = personMapper;
    }

    @Override
    public ResponseEntity<Void> createPerson(CreatePersonRequest request) {
        log.info("Received request to create person: {}", request);
        // Converte DTO para Objeto de Domínio Puro
        Person person = personMapper.toDomain(request);

        // Executa o caso de uso
        createPersonUseCase.execute(person);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
