package org.example.nidabutik.repository;

import org.example.nidabutik.entity.Gender;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenderRepository extends JpaRepository<Gender, Long> {
    Optional<Gender> findByCodeIgnoreCase(String code);
    boolean existsByCodeIgnoreCase(String code);
}
