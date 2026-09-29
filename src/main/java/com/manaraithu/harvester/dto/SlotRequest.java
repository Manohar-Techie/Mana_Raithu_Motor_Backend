package com.manaraithu.harvester.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public record SlotRequest(
    @NotBlank String machineNumber,
    @NotBlank String machineType,
    @NotNull @FutureOrPresent LocalDate slotDate,
    @NotNull LocalTime startTime,
    @NotNull LocalTime endTime,
    @Min(1) int availableTrips,
    @NotNull @DecimalMin("0.01") BigDecimal pricePerTrip
) { }
