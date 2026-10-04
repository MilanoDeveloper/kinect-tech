package com.kinect.orchestrator.adapters.inbound.controller;

import com.kinect.contracts.orchestrator.api.PersonsApi;
import com.kinect.contracts.persons.dto.CreatePersonRequest;
import com.kinect.contracts.persons.dto.GetPersonsApi;
import com.kinect.orchestrator.application.port.in.GymOperationsUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PersonsController implements PersonsApi {
    private final GymOperationsUseCase useCase;

    public PersonsController(GymOperationsUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public ResponseEntity<Void> createPerson(CreatePersonRequest request) {
        useCase.createPerson(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<List<GetPersonsApi>> getPersons() {
        return ResponseEntity.ok(useCase.findPersons());
    }

    @Override
    public ResponseEntity<GetPersonsApi> getPersonById(Long personId) {
        return ResponseEntity.ok(useCase.findPerson(personId));
    }

    @Override
    public ResponseEntity<Void> updatePerson(Long personId, CreatePersonRequest request) {
        useCase.updatePerson(personId, request);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deletePerson(Long personId) {
        useCase.deletePerson(personId);
        return ResponseEntity.noContent().build();
    }
}
