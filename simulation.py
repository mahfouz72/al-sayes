import mysql.connector
import random
import time

# Database connection details
db_config = {
    'host': 'localhost',
    'user': 'root',
    'password': 'root',
    'database': 'alsayesdb'
}

# Connect to the database
def connect_to_db():
    try:
        connection = mysql.connector.connect(**db_config)
        if connection.is_connected():
            print("Connected to the database")
            return connection
    except mysql.connector.Error as err:
        print(f"Error: {err}")
        return None

# Fetch and store the parking spots in memory
def fetch_parking_spots(connection):
    cursor = connection.cursor(dictionary=True)
    try:
        cursor.execute("SELECT id, lot_id, current_status FROM ParkingSpot")
        spots = cursor.fetchall()
        return spots
    except mysql.connector.Error as err:
        print(f"Error: {err}")
        return []
    finally:
        cursor.close()

def update_parking_spots_status(connection, parking_spots, delay=5):
    cursor = connection.cursor()
    try:
        for spot in parking_spots:
            current_status = spot['current_status']
            new_status = 'OCCUPIED' if current_status == 'AVAILABLE' else 'AVAILABLE'
            spot['current_status'] = new_status
            print(f"Updated spot {spot['id']} in lot {spot['lot_id']} to {new_status}")
            update_query = """
                UPDATE ParkingSpot
                SET current_status = %s
                WHERE id = %s AND lot_id = %s
            """
            cursor.execute(update_query, (spot['current_status'], spot['id'], spot['lot_id']))
            time.sleep(delay)
        connection.commit()
        print("Updated parking spot statuses successfully")
    except mysql.connector.Error as err:
        print(f"Error: {err}")
    finally:
        cursor.close()

def simulate_IoT_device(delay1=5, delay2=10):
    print("Starting the simulation")
    connection = connect_to_db()
    if connection:
        try:
            parking_spots = fetch_parking_spots(connection)
            print(parking_spots)
            while True:
                update_parking_spots_status(connection, parking_spots, delay1)
                time.sleep(delay2)
        except KeyboardInterrupt:
            print("Stopping the simulation")
        finally:
            connection.close()

if __name__ == "__main__":
    simulate_IoT_device()
