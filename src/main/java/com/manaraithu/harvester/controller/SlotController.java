package com.manaraithu.harvester.controller;

import com.manaraithu.harvester.dto.SlotRequest;
import com.manaraithu.harvester.model.Slot;
import com.manaraithu.harvester.repository.SlotRepository;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class SlotController {
    private final SlotRepository slotRepository;

    public SlotController(SlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }

    @GetMapping("/slots/available")
    public List<Slot> availableSlots(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return slotRepository.findBySlotDateAndStatusOrderByStartTime(date, "AVAILABLE").stream()
            .filter(slot -> slot.getAvailableTrips() > slot.getBookedTrips())
            .toList();
    }

    @PostMapping("/admin/slots")
    public Slot createSlot(@Valid @RequestBody SlotRequest request) {
        if (!request.endTime().isAfter(request.startTime())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End time must be after start time");
        }
        Slot slot = new Slot();
        slot.setMachineNumber(request.machineNumber().trim());
        slot.setMachineType(request.machineType().trim().toUpperCase());
        slot.setSlotDate(request.slotDate());
        slot.setStartTime(request.startTime());
        slot.setEndTime(request.endTime());
        slot.setAvailableTrips(request.availableTrips());
        slot.setBookedTrips(0);
        slot.setPricePerTrip(request.pricePerTrip());
        slot.setStatus("AVAILABLE");
        return slotRepository.save(slot);
    }
}
