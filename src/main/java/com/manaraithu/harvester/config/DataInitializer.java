package com.manaraithu.harvester.config;

import com.manaraithu.harvester.model.Slot;
import com.manaraithu.harvester.repository.SlotRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner seedDemoSlots(SlotRepository slots) {
        return args -> {
            if (slots.count() > 0) return;
            LocalDate date = LocalDate.now().plusDays(1);
            createSlot(slots, "H-001", date, LocalTime.of(6, 0), LocalTime.of(10, 0), 5);
            createSlot(slots, "H-001", date, LocalTime.of(10, 0), LocalTime.of(14, 0), 5);
            createSlot(slots, "H-002", date, LocalTime.of(14, 0), LocalTime.of(18, 0), 4);
        };
    }

    private void createSlot(SlotRepository slots, String machine, LocalDate date, LocalTime start, LocalTime end, int trips) {
        Slot slot = new Slot();
        slot.setMachineNumber(machine);
        slot.setMachineType("HARVESTER");
        slot.setSlotDate(date);
        slot.setStartTime(start);
        slot.setEndTime(end);
        slot.setAvailableTrips(trips);
        slot.setBookedTrips(0);
        slot.setPricePerTrip(new BigDecimal("2500.00"));
        slot.setStatus("AVAILABLE");
        slots.save(slot);
    }
}
