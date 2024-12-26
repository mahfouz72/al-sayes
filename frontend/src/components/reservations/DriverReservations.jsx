import {useEffect, useState} from "react";
import ReservationList from "./ReservationList";
import axios from "axios";

export default function DriverReservations() {
    const [reservations, setReservations] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const user = localStorage.getItem("username");

    useEffect(() => {
        const fetchReservations = async () => {
            try {
                const token = localStorage.getItem("token");
                const headers = {
                    'Authorization': `Bearer ${token}`,
                };
                const response = await axios.get("http://localhost:8080/drivers/reservations", { headers });
                const data = response.data;

                const reservationsWithId = data.map((reservation, index) => ({
                    ...reservation,
                    id: index + 1,
                }));

                setReservations(reservationsWithId);
            } catch (error) {
                console.error("Error fetching reservations:", error);
            } finally {
                setIsLoading(false);
            }
        };
        fetchReservations();
    }, [user.id]);

    return (
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
            <div className="mb-8">
                <h1 className="text-2xl font-bold text-gray-900">
                    My Reservations
                </h1>
                <p className="mt-2 text-gray-600">
                    View and manage your parking reservations
                </p>
            </div>

            <ReservationList
                reservations={reservations}
                isLoading={isLoading}
            />
        </div>
    );
}
