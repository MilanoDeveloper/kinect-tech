package com.kinect.persons.adapters.mapper;

import com.kinect.contracts.persons.dto.CreatePersonRequest;
import com.kinect.contracts.persons.dto.GetPersonsApi;
import com.kinect.persons.adapters.outbound.repository.entity.PersonEntity;
import com.kinect.persons.core.domain.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(source = "username", target = "username")
    Person toDomain(CreatePersonRequest request);

    PersonEntity toEntity(Person person);

    Person toDomain(PersonEntity entity);

    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "toOffsetDateTime")
    @Mapping(target = "updatedAt", source = "updatedAt", qualifiedByName = "toOffsetDateTime")
    GetPersonsApi toResponse(Person person);

    @Named("toOffsetDateTime")
    default OffsetDateTime toOffsetDateTime(LocalDateTime value) {
        return value == null ? null : value.atOffset(ZoneOffset.UTC);
    }
}