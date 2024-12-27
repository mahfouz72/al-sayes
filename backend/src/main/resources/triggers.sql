DROP TRIGGER IF EXISTS after_parking_lot_insert;
CREATE TRIGGER after_parking_lot_insert
AFTER INSERT ON ParkingLot
FOR EACH ROW
    INSERT INTO Statistics (lot_id)
    VALUES (NEW.id);

DROP TRIGGER IF EXISTS after_parking_lot_delete;
CREATE TRIGGER after_parking_lot_delete
AFTER DELETE ON ParkingLot
FOR EACH ROW
    DELETE FROM Statistics
    WHERE lot_id = OLD.id;

DROP TRIGGER IF EXISTS after_parking_spot_insert;
CREATE TRIGGER after_parking_spot_insert
AFTER INSERT ON ParkingSpot
FOR EACH ROW
    UPDATE Statistics
    SET total_spots = total_spots + 1,
        available_spots = available_spots + IF(NEW.current_status = 'AVAILABLE', 1, 0),
        occupied_spots = occupied_spots + IF(NEW.current_status = 'OCCUPIED', 1, 0),
        regular_spots = regular_spots + IF(NEW.type = 'REGULAR', 1, 0),
        disabled_spots = disabled_spots + IF(NEW.type = 'DISABLED', 1, 0),
        ev_spots = ev_spots + IF(NEW.type = 'EV_CHARGING', 1, 0),
        avg_price = (avg_price * total_spots + NEW.cost) / (total_spots + 1)
    WHERE lot_id = NEW.lot_id;

DROP TRIGGER IF EXISTS after_parking_spot_delete;
CREATE TRIGGER after_parking_spot_delete
AFTER DELETE ON ParkingSpot
FOR EACH ROW
    UPDATE Statistics
    SET total_spots = total_spots - 1,
        available_spots = available_spots - IF(OLD.current_status = 'AVAILABLE', 1, 0),
        occupied_spots = occupied_spots - IF(OLD.current_status = 'OCCUPIED', 1, 0),
        regular_spots = regular_spots - IF(OLD.type = 'REGULAR', 1, 0),
        disabled_spots = disabled_spots - IF(OLD.type = 'DISABLED', 1, 0),
        ev_spots = ev_spots - IF(OLD.type = 'EV_CHARGING', 1, 0),
        avg_price = IFNULL((avg_price * total_spots - OLD.cost) / (total_spots - 1), 0)
    WHERE lot_id = OLD.lot_id;

DROP TRIGGER IF EXISTS after_parking_spot_update;
CREATE TRIGGER after_parking_spot_update
AFTER UPDATE ON ParkingSpot
FOR EACH ROW
    UPDATE Statistics
    SET available_spots = available_spots + IF(NEW.current_status = 'AVAILABLE', 1, 0) - IF(OLD.current_status = 'AVAILABLE', 1, 0),
        occupied_spots = occupied_spots + IF(NEW.current_status = 'OCCUPIED', 1, 0) - IF(OLD.current_status = 'OCCUPIED', 1, 0),
        regular_spots = regular_spots + IF(NEW.type = 'REGULAR', 1, 0) - IF(OLD.type = 'REGULAR', 1, 0),
        disabled_spots = disabled_spots + IF(NEW.type = 'DISABLED', 1, 0) - IF(OLD.type = 'DISABLED', 1, 0),
        ev_spots = ev_spots + IF(NEW.type = 'EV_CHARGING', 1, 0) - IF(OLD.type = 'EV_CHARGING', 1, 0),
        avg_price = IFNULL((avg_price * total_spots - OLD.cost + NEW.cost) / total_spots, 0)
    WHERE lot_id = NEW.lot_id;

DROP TRIGGER IF EXISTS after_reservation_insert;    
CREATE TRIGGER after_reservation_insert
AFTER INSERT ON Reservation
FOR EACH ROW
    UPDATE Statistics
    SET reservations = reservations + 1,
        total_revenue = total_revenue + IF(NEW.status != 'EXPIRED', NEW.price, 0)
    WHERE lot_id = NEW.lot_id;

DROP TRIGGER IF EXISTS after_reservation_delete;
CREATE TRIGGER after_reservation_delete
AFTER DELETE ON Reservation
FOR EACH ROW
    UPDATE Statistics
    SET reservations = reservations - 1,
        total_revenue = total_revenue - IF(OLD.status != 'EXPIRED', OLD.price, 0)
    WHERE lot_id = OLD.lot_id;

DROP TRIGGER IF EXISTS after_reservation_update;
CREATE TRIGGER after_reservation_update
AFTER UPDATE ON Reservation
FOR EACH ROW
    UPDATE Statistics
    SET total_revenue = total_revenue
                            + IF(NEW.status != 'EXPIRED', NEW.price - OLD.price, 0)
                            - IF(NEW.status = 'EXPIRED', NEW.price, 0),
        total_violations = total_violations
                            + IF(OLD.violation_duration = 0 AND NEW.violation_duration > 0, 1, 0) 
                            - IF(OLD.violation_duration > 0 AND NEW.violation_duration = 0, 1, 0)
    WHERE lot_id = NEW.lot_id;

DELIMITER //
DROP TRIGGER IF EXISTS after_parking_spot_update_reservation;
CREATE TRIGGER after_parking_spot_update_reservation
AFTER UPDATE ON ParkingSpot
FOR EACH ROW
BEGIN
    -- Check if the status has changed
    IF NEW.current_status != OLD.current_status THEN
        CALL UpdateReservationStatus(NEW.id, NEW.lot_id, NEW.current_status);
    END IF;
END;
//

DELIMITER ;
