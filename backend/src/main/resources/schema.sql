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
    time_limit decimal NOT NULL,
    automatic_release_time decimal NOT NULL,
    not_showing_up_penalty decimal NOT NULL,
    over_time_scale decimal NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (managed_by) REFERENCES Account(id)
);

CREATE TABLE IF NOT EXISTS ParkingSpot (
    id bigint NOT NULL,
    lot_id bigint NOT NULL,
    cost decimal NOT NULL,
    current_status varchar(255) NOT NULL,
    type varchar(255) NOT NULL,
    PRIMARY KEY (id, lot_id)
);

CREATE TABLE IF NOT EXISTS Reservation (
    driver_id bigint NOT NULL,
    spot_id bigint NOT NULL,
    lot_id bigint NOT NULL,
    start_time timestamp NOT NULL,
    end_time timestamp NOT NULL,
    price decimal NOT NULL,
    status varchar(255) NOT NULL,
    violation_duration decimal NOT NULL,
    penalty decimal NOT NULL,
    PRIMARY KEY (driver_id, spot_id, lot_id, start_time)
);

CREATE TABLE IF NOT EXISTS Driver (
    account_id BIGINT NOT NULL,
    license_plate VARCHAR(255) NOT NULL,
    payment_method ENUM('CASH', 'VISA') NOT NULL,
    PRIMARY KEY (account_id),
    FOREIGN KEY (account_id) REFERENCES Account(id) ON DELETE CASCADE
);

