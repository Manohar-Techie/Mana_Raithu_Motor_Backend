package com.manaraithu.harvester.dto;

import com.manaraithu.harvester.model.BookingStatus;
import jakarta.validation.constraints.NotNull;

public record BookingStatusRequest(@NotNull BookingStatus status) { }
