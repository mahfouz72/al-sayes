CREATE INDEX idx_managed_by ON ParkingLot(managed_by);
CREATE INDEX idx_lot_id ON ParkingSpot(lot_id);
CREATE INDEX idx_reservation_spot_lot ON Reservation (spot_id, lot_id);
CREATE INDEX idx_reservation_status ON Reservation (status);
CREATE INDEX idx_reservation_start_time ON Reservation (start_time);
CREATE INDEX idx_reservation_end_time ON Reservation (end_time);