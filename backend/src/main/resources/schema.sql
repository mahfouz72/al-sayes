CREATE TABLE IF NOT EXISTS ParkingLot (
    id bigint NOT NULL AUTO_INCREMENT,
    location varchar(1024) NOT NULL,
    capacity int NOT NULL,
    time_limit decimal NOT NULL,
    automatic_release_time decimal NOT NULL,
    not_showing_up_penalty decimal NOT NULL,
    over_time_scale decimal NOT NULL,
    PRIMARY KEY (id)
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
