package com.manaraithu.harvester.controller;

import com.manaraithu.harvester.dto.BookingRequest;
import com.manaraithu.harvester.dto.BookingStatusRequest;
import com.manaraithu.harvester.model.Booking;
import com.manaraithu.harvester.service.BookingService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/bookings")
    @ResponseStatus(HttpStatus.CREATED)
    public Booking createBooking(@Valid @RequestBody BookingRequest request) {
        return bookingService.create(request);
    }

    @GetMapping("/admin/bookings")
    public List<Booking> listBookings() {
        return bookingService.listAll();
    }

    @PatchMapping("/admin/bookings/{id}/status")
    public Booking updateStatus(@PathVariable Long id, @Valid @RequestBody BookingStatusRequest request) {
        return bookingService.updateStatus(id, request.status());
    }
}
