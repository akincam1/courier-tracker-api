package org.migros.adapter.courier.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CourierLogLocationRequest(
        @NotBlank String courierId,
        @NotNull Double lat,
        @NotNull Double lng,
        @NotNull LocalDateTime time) {
}
