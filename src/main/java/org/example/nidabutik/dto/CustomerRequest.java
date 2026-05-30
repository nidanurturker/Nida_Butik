package org.example.nidabutik.dto;

import jakarta.validation.constraints.*;

public record CustomerRequest(
        @NotBlank @Size(max = 80) String firstName,
        @NotBlank @Size(max = 80) String lastName,
        @NotBlank @Email @Size(max = 140) String email,
        @Size(max = 30) String phone,
        @NotBlank @Size(max = 20) String gender,
        @NotBlank @Size(max = 240) String address,
        @NotBlank @Size(max = 80) String city
) {
}
