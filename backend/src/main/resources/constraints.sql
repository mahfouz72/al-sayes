ALTER TABLE ParkingLot
ADD CONSTRAINT chk_parkinglot_positive_values
CHECK (time_limit >= 15 AND automatic_release_time >= 0 AND not_showing_up_penalty >= 0 AND over_time_scale >= 1);

ALTER TABLE ParkingSpot
ADD CONSTRAINT chk_parkingspot_cost CHECK (cost >= 0);

ALTER TABLE Reservation
ADD CONSTRAINT chk_reservation_time CHECK (start_time < end_time);

ALTER TABLE Reservation
ADD CONSTRAINT chk_reservation_positive_values
CHECK (price >= 0 AND violation_duration >= 0 AND penalty >= 0);
