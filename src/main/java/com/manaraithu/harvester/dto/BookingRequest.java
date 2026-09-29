package com.manaraithu.harvester.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record BookingRequest(
    @NotNull Long slotId,
    @Positive int numberOfTrips,
    @NotNull @DecimalMin("0.1") BigDecimal landAreaAcres,
    @NotNull @DecimalMin("0.5") BigDecimal estimatedHours,
    @NotBlank String customerName,
    @NotBlank @Pattern(regexp = "[0-9]{10}") String mobileNumber
) { }
