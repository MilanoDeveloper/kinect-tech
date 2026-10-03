package com.kinect.orchestrator.application.port.out;

import com.kinect.contracts.persons.dto.CreatePersonRequest;
import com.kinect.contracts.persons.dto.GetPersonsApi;

import java.util.List;

public interface PersonsServicePort {
    void createPerson(CreatePersonRequest request);
    List<GetPersonsApi> findAllPersons();
    GetPersonsApi findPersonById(Long personId);
    void updatePerson(Long personId, CreatePersonRequest request);
    void deletePerson(Long personId);
}
