CREATE TABLE IF NOT EXISTS Account (
    id BIGINT NOT NULL AUTO_INCREMENT,
    username varchar(45) NOT NULL,
    email varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    role_name ENUM('ROLE_DRIVER', 'ROLE_MANAGER', 'ROLE_ADMIN') NOT NULL,
    status ENUM('BLOCKED', 'ACTIVE') NOT NULL,
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
    current_status ENUM('AVAILABLE', 'OCCUPIED', 'FAULTY') NOT NULL,
    type ENUM('REGULAR', 'DISABLED', 'EV_CHARGING') NOT NULL,
    PRIMARY KEY (id, lot_id),
    FOREIGN KEY (lot_id) REFERENCES ParkingLot(id) ON DELETE CASCADE
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
