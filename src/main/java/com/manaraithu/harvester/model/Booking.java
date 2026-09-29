package com.manaraithu.harvester.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 24)
    private String bookingNumber;

    @Column(nullable = false)
    private Long slotId;

    @Column(nullable = false, length = 32)
    private String machineNumber;

    @Column(nullable = false, length = 100)
    private String customerName;

    @Column(nullable = false, length = 10)
    private String mobileNumber;

    @Column(nullable = false)
    private LocalDate bookingDate;

    @Column(nullable = false)
    private int numberOfTrips;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal landAreaAcres;

    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal estimatedHours;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal estimatedAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookingStatus status = BookingStatus.REQUESTED;

    @Column(length = 500)
    private String customerRemarks;

    @Column(nullable = false)
    private Instant createdAt;

    public Booking() { }

    @PrePersist
    void beforeInsert() {
        if (bookingNumber == null) bookingNumber = "BK" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        if (createdAt == null) createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public String getBookingNumber() { return bookingNumber; }
    public Long getSlotId() { return slotId; }
    public String getMachineNumber() { return machineNumber; }
    public String getCustomerName() { return customerName; }
    public String getMobileNumber() { return mobileNumber; }
    public LocalDate getBookingDate() { return bookingDate; }
    public int getNumberOfTrips() { return numberOfTrips; }
    public BigDecimal getLandAreaAcres() { return landAreaAcres; }
    public BigDecimal getEstimatedHours() { return estimatedHours; }
    public BigDecimal getEstimatedAmount() { return estimatedAmount; }
    public BookingStatus getStatus() { return status; }
    public String getCustomerRemarks() { return customerRemarks; }
    public Instant getCreatedAt() { return createdAt; }

    public void setSlotId(Long slotId) { this.slotId = slotId; }
    public void setMachineNumber(String machineNumber) { this.machineNumber = machineNumber; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }
    public void setNumberOfTrips(int numberOfTrips) { this.numberOfTrips = numberOfTrips; }
    public void setLandAreaAcres(BigDecimal landAreaAcres) { this.landAreaAcres = landAreaAcres; }
    public void setEstimatedHours(BigDecimal estimatedHours) { this.estimatedHours = estimatedHours; }
    public void setEstimatedAmount(BigDecimal estimatedAmount) { this.estimatedAmount = estimatedAmount; }
    public void setStatus(BookingStatus status) { this.status = status; }
    public void setCustomerRemarks(String customerRemarks) { this.customerRemarks = customerRemarks; }
}
