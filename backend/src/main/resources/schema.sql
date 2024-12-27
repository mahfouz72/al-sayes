CREATE TABLE IF NOT EXISTS Account (
    id BIGINT NOT NULL AUTO_INCREMENT,
    username varchar(45) NOT NULL,
    email varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    role_name varchar(45) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY (username)
);

CREATE TABLE IF NOT EXISTS ParkingLot (
    id bigint NOT NULL AUTO_INCREMENT,
    name varchar(255) NOT NULL,
    managed_by bigint NOT NULL,
    location varchar(1024) NOT NULL,
    latitude decimal(10, 6) NOT NULL,
    longitude decimal(10, 6) NOT NULL,
    time_limit decimal(10, 6) NOT NULL,
    automatic_release_time decimal(10, 6) NOT NULL,
    not_showing_up_penalty decimal(10, 6) NOT NULL,
    over_time_scale decimal(10, 6) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (managed_by) REFERENCES Account(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS ParkingSpot (
    id bigint NOT NULL,
    lot_id bigint NOT NULL,
    cost decimal(10, 6) NOT NULL,
    current_status varchar(255) NOT NULL,
    type varchar(255) NOT NULL,
    PRIMARY KEY (id, lot_id),
    FOREIGN KEY (lot_id) REFERENCES ParkingLot(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Driver (
    account_id BIGINT NOT NULL,
    license_plate VARCHAR(255) NOT NULL,
    PRIMARY KEY (account_id),
    FOREIGN KEY (account_id) REFERENCES Account(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Reservation (
    driver_id bigint NOT NULL,
    spot_id bigint NOT NULL,
    lot_id bigint NOT NULL,
    start_time timestamp NOT NULL,
    end_time timestamp NOT NULL,
    price decimal(10, 6) NOT NULL,
    status varchar(255) NOT NULL,
    violation_duration decimal(10, 6) NOT NULL,
    penalty decimal(10, 6) NOT NULL,
    PRIMARY KEY (driver_id, spot_id, lot_id, start_time),
    FOREIGN KEY (driver_id) REFERENCES Account(id) ON DELETE CASCADE,
    FOREIGN KEY (spot_id, lot_id) REFERENCES ParkingSpot(id, lot_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Driver (
    account_id BIGINT NOT NULL,
    license_plate VARCHAR(255) NOT NULL,
    payment_method ENUM('CASH', 'VISA') NOT NULL,
    PRIMARY KEY (account_id),
    FOREIGN KEY (account_id) REFERENCES Account(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Statistics (
    lot_id BIGINT NOT NULL,
    total_spots INT DEFAULT 0,
    occupied_spots INT DEFAULT 0,
    available_spots INT DEFAULT 0,
    regular_spots INT DEFAULT 0,
    disabled_spots INT DEFAULT 0,
    ev_spots INT DEFAULT 0,
    avg_price DECIMAL(10, 6) DEFAULT 0.0,
    reservations INT DEFAULT 0,
    total_revenue DECIMAL(10, 6) DEFAULT 0.0,
    total_violations INT DEFAULT 0,
    PRIMARY KEY (lot_id),
    FOREIGN KEY (lot_id) REFERENCES ParkingLot(id) ON DELETE CASCADE
);


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


