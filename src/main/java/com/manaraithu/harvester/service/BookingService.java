package com.manaraithu.harvester.service;

import com.manaraithu.harvester.dto.BookingRequest;
import com.manaraithu.harvester.model.Booking;
import com.manaraithu.harvester.model.BookingStatus;
import com.manaraithu.harvester.model.Slot;
import com.manaraithu.harvester.repository.BookingRepository;
import com.manaraithu.harvester.repository.SlotRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final SlotRepository slotRepository;

    public BookingService(BookingRepository bookingRepository, SlotRepository slotRepository) {
        this.bookingRepository = bookingRepository;
        this.slotRepository = slotRepository;
    }

    @Transactional
    public Booking create(BookingRequest request) {
        Slot slot = slotRepository.findByIdForUpdate(request.slotId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Slot not found"));
        int remainingTrips = slot.getAvailableTrips() - slot.getBookedTrips();
        if (!"AVAILABLE".equals(slot.getStatus()) || request.numberOfTrips() > remainingTrips) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The selected slot no longer has enough trips available");
        }
        if (slot.getSlotDate().isBefore(java.time.LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Bookings cannot be made for past dates");
        }

        Booking booking = new Booking();
        booking.setSlotId(slot.getId());
        booking.setMachineNumber(slot.getMachineNumber());
        booking.setCustomerName(request.customerName().trim());
        booking.setMobileNumber(request.mobileNumber());
        booking.setBookingDate(slot.getSlotDate());
        booking.setNumberOfTrips(request.numberOfTrips());
        booking.setLandAreaAcres(request.landAreaAcres());
        booking.setEstimatedHours(request.estimatedHours());
        booking.setEstimatedAmount(slot.getPricePerTrip().multiply(BigDecimal.valueOf(request.numberOfTrips())));
        booking.setStatus(BookingStatus.REQUESTED);
        slot.reserveTrips(request.numberOfTrips());
        slotRepository.save(slot);
        return bookingRepository.save(booking);
    }

    @Transactional(readOnly = true)
    public List<Booking> listAll() {
        return bookingRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional
    public Booking updateStatus(Long id, BookingStatus status) {
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));
        BookingStatus currentStatus = booking.getStatus();
        if (currentStatus == BookingStatus.COMPLETED || currentStatus == BookingStatus.REJECTED || currentStatus == BookingStatus.CANCELLED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A completed or closed booking cannot be changed");
        }
        if ((status == BookingStatus.REJECTED || status == BookingStatus.CANCELLED)
                && currentStatus != BookingStatus.REJECTED && currentStatus != BookingStatus.CANCELLED) {
            Slot slot = slotRepository.findByIdForUpdate(booking.getSlotId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking slot not found"));
            slot.releaseTrips(booking.getNumberOfTrips());
            slotRepository.save(slot);
        }
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }
}
