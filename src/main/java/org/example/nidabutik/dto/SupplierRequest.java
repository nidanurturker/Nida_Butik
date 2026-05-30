package org.example.nidabutik.dto;

import jakarta.validation.constraints.*;

public record SupplierRequest(
        @NotBlank @Size(max = 120) String name,
        @NotBlank @Email @Size(max = 140) String email,
        @Size(max = 30) String phone,
        @Size(max = 120) String city
) {
}
