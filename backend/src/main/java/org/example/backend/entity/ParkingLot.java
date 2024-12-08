package org.example.backend.entity;

public class ParkingLot {
    private Long id;
    private int capacity;
    private String location;

    // Longest possible reservation duration
    private double timeLimit;

    // Duration of not showing up, so that the reservation is automatically released
    private double automaticReleaseTime;

    // Penalty of not showing up
    private double notShowingUpPenalty;

    // Penalty for staying parked over reserved time (scale per extra hour)
    private double overTimeScale;

    public ParkingLot() {}
    public ParkingLot(int capacity, String location, double timeLimit, double automaticReleaseTime, double notShowingUpPenalty, double overTimeScale) {
        this.capacity = capacity;
        this.location = location;
        this.timeLimit = timeLimit;
        this.automaticReleaseTime = automaticReleaseTime;
        this.notShowingUpPenalty = notShowingUpPenalty;
        this.overTimeScale = overTimeScale;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(double timeLimit) {
        this.timeLimit = timeLimit;
    }

    public double getAutomaticReleaseTime() {
        return automaticReleaseTime;
    }

    public void setAutomaticReleaseTime(double automaticReleaseTime) {
        this.automaticReleaseTime = automaticReleaseTime;
    }

    public double getNotShowingUpPenalty() {
        return notShowingUpPenalty;
    }

    public void setNotShowingUpPenalty(double notShowingUpPenalty) {
        this.notShowingUpPenalty = notShowingUpPenalty;
    }

    public double getOverTimeScale() {
        return overTimeScale;
    }

    public void setOverTimeScale(double overTimeScale) {
        this.overTimeScale = overTimeScale;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
