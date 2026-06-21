package com.kinect.persons.adapters.mapper;

import com.kinect.contracts.persons.dto.CreatePersonRequest;
import com.kinect.persons.adapters.outbound.repository.entity.PersonEntity;
import com.kinect.persons.core.domain.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    // Converte o DTO da lib de contratos para o domínio interno
    @Mapping(target = "id", ignore = true) // Ignora o ID, pois será gerado pelo banco
    @Mapping(target = "username", source = "username")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "birthDate", source = "birthDate")
    @Mapping(target = "gender", source = "gender")
    @Mapping(target = "personType", source = "personType")
    @Mapping(target = "internalPersonal", source = "internalPersonal")
    @Mapping(target = "cref", source = "cref")
    @Mapping(target = "cpf", source = "cpf")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "note", source = "note")
    @Mapping(target = "createdAt", ignore = true) // Ignora, será gerenciado pelo banco ou lógica de negócio
    @Mapping(target = "updatedAt", ignore = true) // Ignora, será gerenciado pelo banco ou lógica de negócio
    Person toDomain(CreatePersonRequest request);

    // Converte o domínio interno para a Entidade que vai pro banco
    @Mapping(target = "id", source = "id")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "birthDate", source = "birthDate")
    @Mapping(target = "gender", source = "gender")
    @Mapping(target = "personType", source = "personType")
    @Mapping(target = "internalPersonal", source = "internalPersonal")
    @Mapping(target = "cref", source = "cref")
    @Mapping(target = "cpf", source = "cpf")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "note", source = "note")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    PersonEntity toEntity(Person person);

    // Converte a Entidade do banco de volta para o domínio interno
    Person toDomain(PersonEntity entity);
}