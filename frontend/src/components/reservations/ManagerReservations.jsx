import { useState, useEffect } from "react";
import axios from "axios";
import ReservationList from "./ReservationList";

export default function ManagerReservations() {
    const [reservations, setReservations] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [selectedLot, setSelectedLot] = useState("all");
    const user = localStorage.getItem("username");

    const parkingLots = ["Downtown Parking", "Mall Parking"];

    useEffect(() => {
        const fetchReservations = async () => {
            try {
                const token = localStorage.getItem("token");
                const headers = {
                    'Authorization': `Bearer ${token}`,
                };
                const response = await axios.get("http://localhost:8080/lot-manager/reservations", { headers });
                const data = response.data;

                const reservationsWithId = data.map((reservation, index) => ({
                    id: index + 1,
                    ...reservation,
                }));

                const filteredReservations =
                    selectedLot === "all"
                        ? reservationsWithId
                        : reservationsWithId.filter((r) => r.parkingLot === selectedLot);

                setReservations(filteredReservations);
            } catch (error) {
                console.error("Error fetching reservations:", error);
            } finally {
                setIsLoading(false);
            }
        };

        fetchReservations();
    }, [selectedLot, user.id]);

    return (
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
            <div className="mb-8">
                <h1 className="text-2xl font-bold text-gray-900">
                    Parking Lot Reservations
                </h1>
                <p className="mt-2 text-gray-600">
                    Manage reservations for your parking lots
                </p>
            </div>

            <div className="mb-6">
                <label
                    htmlFor="parking-lot"
                    className="block text-sm font-medium text-gray-700"
                >
                    Filter by Parking Lot
                </label>
                <select
                    id="parking-lot"
                    value={selectedLot}
                    onChange={(e) => setSelectedLot(e.target.value)}
                    className="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm rounded-md"
                >
                    <option value="all">All Parking Lots</option>
                    {parkingLots.map((lot) => (
                        <option key={lot} value={lot}>
                            {lot}
                        </option>
                    ))}
                </select>
            </div>

            <ReservationList
                reservations={reservations}
                isLoading={isLoading}
            />
        </div>
    );
}
