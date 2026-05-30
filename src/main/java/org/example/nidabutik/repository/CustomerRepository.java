package org.example.nidabutik.repository;

import org.example.nidabutik.entity.Customer;
import org.example.nidabutik.entity.Gender;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmailIgnoreCase(String email);
    List<Customer> findByGender(Gender gender);
    List<Customer> findTop8ByGenderOrderByCreatedAtDesc(Gender gender);
    boolean existsByEmailIgnoreCase(String email);
}
