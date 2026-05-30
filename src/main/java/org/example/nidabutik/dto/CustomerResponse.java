package org.example.nidabutik.dto;

import java.time.LocalDateTime;

public record CustomerResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String genderCode,
        String genderLabel,
        String address,
        String city,
        LocalDateTime createdAt
) {
}
