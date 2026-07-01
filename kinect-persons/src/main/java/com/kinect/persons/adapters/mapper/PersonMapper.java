package com.kinect.persons.adapters.mapper;

import com.kinect.contracts.persons.dto.CreatePersonRequest;
import com.kinect.contracts.persons.dto.GetPersonsApi;
import com.kinect.persons.adapters.outbound.repository.entity.PersonEntity;
import com.kinect.persons.core.domain.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(source = "username", target = "username")
    Person toDomain(CreatePersonRequest request);

    PersonEntity toEntity(Person person);

    Person toDomain(PersonEntity entity);

    GetPersonsApi toResponse(Person person);
}