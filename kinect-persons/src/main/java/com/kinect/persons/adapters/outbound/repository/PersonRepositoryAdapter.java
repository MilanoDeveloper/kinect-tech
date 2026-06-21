package com.kinect.persons.adapters.outbound.repository;

import com.kinect.persons.adapters.mapper.PersonMapper;
import com.kinect.persons.adapters.outbound.repository.entity.PersonEntity;
import com.kinect.persons.core.domain.Person;
import com.kinect.persons.core.ports.outbound.PersonOutputPort;
import org.springframework.stereotype.Component;

@Component
public class PersonRepositoryAdapter implements PersonOutputPort {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    public PersonRepositoryAdapter(PersonRepository personRepository, PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
    }

    @Override
    public Person save(Person person) {
        PersonEntity entityToSave = personMapper.toEntity(person);
        PersonEntity savedEntity = personRepository.save(entityToSave);
        return personMapper.toDomain(savedEntity);
    }

    @Override
    public boolean existsByUsername(String username) {
        return personRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByCpf(String cpf) {
        return personRepository.existsByCpf(cpf);
    }

    @Override
    public boolean existsByEmail(String email) {
        return personRepository.existsByEmail(email);
    }
}