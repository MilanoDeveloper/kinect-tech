package com.kinect.persons.adapters.inbound.controller;

import com.kinect.contracts.persons.api.PersonsApi;
import com.kinect.contracts.persons.dto.CreatePersonRequest;
import com.kinect.contracts.persons.dto.GetPersonsApi;
import com.kinect.persons.adapters.mapper.PersonMapper;
import com.kinect.persons.core.domain.Person;
import com.kinect.persons.core.ports.inbound.CreatePersonUseCase;
import com.kinect.persons.core.ports.inbound.FindAllPersonsUseCase;
import com.kinect.persons.core.ports.inbound.ManagePersonsUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@RestController
public class PersonController implements PersonsApi {

    private final CreatePersonUseCase createPersonUseCase;
    private final FindAllPersonsUseCase findAllPersonsUseCase;
    private final ManagePersonsUseCase managePersonsUseCase;
    private final PersonMapper personMapper;

    public PersonController(CreatePersonUseCase createPersonUseCase,
                            FindAllPersonsUseCase findAllPersonsUseCase,
                            ManagePersonsUseCase managePersonsUseCase,
                            PersonMapper personMapper) {
        this.createPersonUseCase = createPersonUseCase;
        this.findAllPersonsUseCase = findAllPersonsUseCase;
        this.managePersonsUseCase = managePersonsUseCase;
        this.personMapper = personMapper;
    }

    @Override
    public ResponseEntity<Void> createPerson(CreatePersonRequest request) {
        log.info("Received request to create person with username: {}", request.getUsername());
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
                .toList();
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<GetPersonsApi> getPersonById(Long personId) {
        Person person = managePersonsUseCase.findById(personId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found"));
        return ResponseEntity.ok(personMapper.toResponse(person));
    }

    @Override
    public ResponseEntity<Void> updatePerson(Long personId, CreatePersonRequest request) {
        Person person = personMapper.toDomain(request);
        if (!managePersonsUseCase.update(personId, person)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found");
        }
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deletePerson(Long personId) {
        if (!managePersonsUseCase.delete(personId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found");
        }
        return ResponseEntity.noContent().build();
    }
}