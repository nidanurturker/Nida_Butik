package org.example.nidabutik.dto;

import jakarta.validation.constraints.*;
import org.example.nidabutik.entity.Gender;

public record CustomerRequest(
        @NotBlank @Size(max = 80) String firstName,
        @NotBlank @Size(max = 80) String lastName,
        @NotBlank @Email @Size(max = 140) String email,
        @Size(max = 30) String phone,
        @NotNull Gender gender,
        @NotBlank @Size(max = 240) String address,
        @NotBlank @Size(max = 80) String city
) {
}
