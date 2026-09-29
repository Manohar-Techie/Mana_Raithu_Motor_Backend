package com.manaraithu.harvester.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "slots")
public class Slot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 32)
    private String machineNumber;

    @Column(nullable = false, length = 24)
    private String machineType = "HARVESTER";

    @Column(nullable = false)
    private LocalDate slotDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private int availableTrips;

    @Column(nullable = false)
    private int bookedTrips = 0;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerTrip;

    @Column(nullable = false, length = 20)
    private String status = "AVAILABLE";

    public Slot() { }

    public Long getId() { return id; }
    public String getMachineNumber() { return machineNumber; }
    public String getMachineType() { return machineType; }
    public LocalDate getSlotDate() { return slotDate; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public int getAvailableTrips() { return availableTrips; }
    public int getBookedTrips() { return bookedTrips; }
    public BigDecimal getPricePerTrip() { return pricePerTrip; }
    public String getStatus() { return status; }

    public void setMachineNumber(String machineNumber) { this.machineNumber = machineNumber; }
    public void setMachineType(String machineType) { this.machineType = machineType; }
    public void setSlotDate(LocalDate slotDate) { this.slotDate = slotDate; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    public void setAvailableTrips(int availableTrips) { this.availableTrips = availableTrips; }
    public void setBookedTrips(int bookedTrips) { this.bookedTrips = bookedTrips; }
    public void setPricePerTrip(BigDecimal pricePerTrip) { this.pricePerTrip = pricePerTrip; }
    public void setStatus(String status) { this.status = status; }

    public void reserveTrips(int trips) {
        this.bookedTrips += trips;
        if (this.bookedTrips >= this.availableTrips) this.status = "FULL";
    }

    public void releaseTrips(int trips) {
        this.bookedTrips = Math.max(0, this.bookedTrips - trips);
        if ("FULL".equals(this.status) && this.bookedTrips < this.availableTrips) this.status = "AVAILABLE";
    }
}
