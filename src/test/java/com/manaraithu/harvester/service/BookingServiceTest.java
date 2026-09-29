package com.manaraithu.harvester.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.manaraithu.harvester.dto.BookingRequest;
import com.manaraithu.harvester.model.Booking;
import com.manaraithu.harvester.model.BookingStatus;
import com.manaraithu.harvester.model.Slot;
import com.manaraithu.harvester.repository.BookingRepository;
import com.manaraithu.harvester.repository.SlotRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {
    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private SlotRepository slotRepository;

    @InjectMocks
    private BookingService bookingService;

    private Slot slot;

    @BeforeEach
    void setUp() {
        slot = new Slot();
        slot.setMachineNumber("H-001");
        slot.setMachineType("HARVESTER");
        slot.setSlotDate(LocalDate.now().plusDays(1));
        slot.setStartTime(LocalTime.of(6, 0));
        slot.setEndTime(LocalTime.of(10, 0));
        slot.setAvailableTrips(5);
        slot.setBookedTrips(0);
        slot.setPricePerTrip(new BigDecimal("2500.00"));
        slot.setStatus("AVAILABLE");
    }

    @Test
    void createCalculatesAmountAndReservesCapacity() {
        when(slotRepository.findByIdForUpdate(7L)).thenReturn(Optional.of(slot));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Booking booking = bookingService.create(request(2));

        assertEquals(new BigDecimal("5000.00"), booking.getEstimatedAmount());
        assertEquals(BookingStatus.REQUESTED, booking.getStatus());
        assertEquals(2, slot.getBookedTrips());
    }

    @Test
    void createRejectsRequestsBeyondRemainingCapacity() {
        slot.setBookedTrips(4);
        when(slotRepository.findByIdForUpdate(7L)).thenReturn(Optional.of(slot));

        assertThrows(ResponseStatusException.class, () -> bookingService.create(request(2)));
        assertEquals(4, slot.getBookedTrips());
    }

    @Test
    void rejectingBookingReturnsTripsToTheSlot() {
        Booking booking = new Booking();
        booking.setSlotId(7L);
        booking.setNumberOfTrips(2);
        booking.setStatus(BookingStatus.REQUESTED);
        slot.setBookedTrips(2);
        when(bookingRepository.findById(11L)).thenReturn(Optional.of(booking));
        when(slotRepository.findByIdForUpdate(7L)).thenReturn(Optional.of(slot));
        when(bookingRepository.save(booking)).thenReturn(booking);

        bookingService.updateStatus(11L, BookingStatus.REJECTED);

        assertEquals(0, slot.getBookedTrips());
        assertEquals("AVAILABLE", slot.getStatus());
    }

    private BookingRequest request(int trips) {
        return new BookingRequest(7L, trips, new BigDecimal("4.5"), new BigDecimal("6"), "Ravi Reddy", "9876543210");
    }
}
