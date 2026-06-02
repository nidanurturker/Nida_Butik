package org.example.nidabutik.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record OrderRequest(
        @NotNull @Positive Long customerId,
        @NotEmpty @Size(min = 1) List<@Valid OrderItemRequest> items
) {
}
