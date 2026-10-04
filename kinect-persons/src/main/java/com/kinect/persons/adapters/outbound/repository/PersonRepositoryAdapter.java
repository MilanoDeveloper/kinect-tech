package com.kinect.persons.adapters.outbound.repository;

import com.kinect.persons.adapters.mapper.PersonMapper;
import com.kinect.persons.adapters.outbound.repository.entity.PersonEntity;
import com.kinect.persons.core.domain.Person;
import com.kinect.persons.core.ports.outbound.PersonOutputPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        if (entityToSave.getMedicalConditions() == null) {
            entityToSave.setMedicalConditions(List.of());
        }
        PersonEntity savedEntity = personRepository.save(entityToSave);
        return personMapper.toDomain(savedEntity);
    }

    @Override
    public List<Person> findAll() {
        return personRepository.findAll()
                .stream()
                .map(personMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Person> findById(Long id) {
        return personRepository.findById(id).map(personMapper::toDomain);
    }

    @Override
    public boolean deleteById(Long id) {
        if (!personRepository.existsById(id)) {
            return false;
        }
        personRepository.deleteById(id);
        return true;
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

    @Override
    public boolean existsByUsernameAndIdNot(String username, Long id) {
        return personRepository.existsByUsernameAndIdNot(username, id);
    }

    @Override
    public boolean existsByCpfAndIdNot(String cpf, Long id) {
        return personRepository.existsByCpfAndIdNot(cpf, id);
    }

    @Override
    public boolean existsByEmailAndIdNot(String email, Long id) {
        return personRepository.existsByEmailAndIdNot(email, id);
    }
}