package com.kinect.persons.adapters.inbound.controller;

import com.kinect.contracts.persons.api.PersonsApi;
import com.kinect.contracts.persons.dto.CreatePersonRequest;
import com.kinect.contracts.persons.dto.GetPersonsApi;
import com.kinect.persons.adapters.mapper.PersonMapper;
import com.kinect.persons.core.domain.Person;
import com.kinect.persons.core.ports.inbound.CreatePersonUseCase;
import com.kinect.persons.core.ports.inbound.FindAllPersonsUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class PersonController implements PersonsApi {

    private final CreatePersonUseCase createPersonUseCase;
    private final FindAllPersonsUseCase findAllPersonsUseCase;
    private final PersonMapper personMapper;

    public PersonController(CreatePersonUseCase createPersonUseCase,
                            FindAllPersonsUseCase findAllPersonsUseCase,
                            PersonMapper personMapper) {
        this.createPersonUseCase = createPersonUseCase;
        this.findAllPersonsUseCase = findAllPersonsUseCase;
        this.personMapper = personMapper;
    }

    @Override
    public ResponseEntity<Void> createPerson(CreatePersonRequest request) {
        log.info("Received request to create person: {}", request);
        Person person = personMapper.toDomain(request);
        createPersonUseCase.execute(person);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<List<GetPersonsApi>> getPersons() {
        log.info("Received request to list all persons");
        List<Person> persons = findAllPersonsUseCase.execute();
        List<GetPersonsApi> response = persons.stream()
                .map(personMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}