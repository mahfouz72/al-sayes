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