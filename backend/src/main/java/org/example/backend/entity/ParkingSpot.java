package org.example.backend.entity;

public class ParkingSpot {
    Long id;
    Long lotId;
    double cost;
    String currentStatus;
    String type;

    public ParkingSpot() {}
    public ParkingSpot(Long lotId, double cost, String currentStatus, String type) {
        this.lotId = lotId;
        this.cost = cost;
        this.currentStatus = currentStatus;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLotId() {
        return lotId;
    }

    public void setLotId(Long lotId) {
        this.lotId = lotId;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
