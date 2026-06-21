package com.kinect.persons.adapters.outbound.repository;

import com.kinect.persons.adapters.outbound.repository.entity.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<PersonEntity, Long> {
    boolean existsByUsername(String username);
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
}