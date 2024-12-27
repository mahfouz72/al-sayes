DELIMITER //
DROP PROCEDURE IF EXISTS UpdateReservationStatus;
CREATE PROCEDURE UpdateReservationStatus(spot_id BIGINT, lot_id BIGINT, new_status VARCHAR(255))
BEGIN
    -- Handle the 'OCCUPIED' status
    IF new_status = 'OCCUPIED' THEN
        -- Use a JOIN to update the reservation status to 'ONGOING'
        UPDATE Reservation r
        JOIN (
            SELECT MAX(start_time) AS latest_start_time
            FROM Reservation
            WHERE spot_id = spot_id
              AND lot_id = lot_id
              AND start_time <= NOW()
              AND end_time > NOW()
              AND status != 'EXPIRED'
              AND status != 'FULFILLED'
        ) AS latest_reservation
        ON r.start_time = latest_reservation.latest_start_time
        SET r.status = 'ONGOING'
        WHERE r.spot_id = spot_id
          AND r.lot_id = lot_id;
    -- Handle the 'AVAILABLE' status
    ELSEIF new_status = 'AVAILABLE' THEN
        -- Use a JOIN to update the reservation status to 'FULFILLED'
        UPDATE Reservation r
        JOIN (
            SELECT MAX(end_time) AS latest_end_time
            FROM Reservation
            WHERE spot_id = spot_id
              AND lot_id = lot_id
              AND status = 'ONGOING'
        ) AS latest_reservation
        ON r.end_time = latest_reservation.latest_end_time
        SET r.status = 'FULFILLED'
        WHERE r.spot_id = spot_id
          AND r.lot_id = lot_id
          AND r.status = 'ONGOING';
    END IF;
END;
//

DELIMITER ;


DELIMITER //

DROP PROCEDURE IF EXISTS ReserveParkingSpot;
CREATE PROCEDURE ReserveParkingSpot(
    IN p_driver_id BIGINT,
    IN p_spot_id BIGINT,
    IN p_lot_id BIGINT,
    IN p_start_time TIMESTAMP,
    IN p_end_time TIMESTAMP,
    IN p_price DECIMAL(10, 6)
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Reservation failed due to a conflict or error';
    END;

    START TRANSACTION;

    -- Lock the Reservation table to prevent other transactions from making changes
SELECT 
    *
FROM
    Reservation
WHERE
    spot_id = p_spot_id
        AND lot_id = p_lot_id
FOR UPDATE;

    -- Check if the spot is already reserved during the desired time
    IF EXISTS (
        SELECT 1 FROM Reservation
        WHERE spot_id = p_spot_id AND lot_id = p_lot_id
        AND (p_start_time < end_time AND p_end_time > start_time)
    ) THEN
        ROLLBACK;
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Spot is already reserved during the requested time';
    END IF;

    -- Insert the reservation
    INSERT INTO Reservation (driver_id, spot_id, lot_id, start_time, end_time, price, status, violation_duration, penalty)
    VALUES (p_driver_id, p_spot_id, p_lot_id, p_start_time, p_end_time, p_price, 'CONFIRMED', 0, 0);

    COMMIT;
END; //
