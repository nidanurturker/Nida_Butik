package org.example.nidabutik.dto;

import org.example.nidabutik.entity.Gender;

import java.time.LocalDateTime;

public record CustomerResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        Gender gender,
        String address,
        String city,
        LocalDateTime createdAt
) {
}
