package org.example.nidabutik.dto;

public record SupplierResponse(
        Long id,
        String name,
        String email,
        String phone,
        String city
) {
}
