import { useState, useEffect } from "react";
import ReservationList from "./ReservationList";
import useAuthStore from "../../store/authStore";
import { mockAdminReservations } from "./mockData";

export default function AdminReservations() {
    const [reservations, setReservations] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [filters, setFilters] = useState({
        parkingLot: "all",
        status: "all",
        dateRange: "all",
    });
    const { user } = useAuthStore();

    const parkingLots = ["Downtown Parking", "Mall Parking", "Airport Parking"];
    const statuses = ["Active", "Completed", "Cancelled"];

    useEffect(() => {
        // Simulate API call
        const fetchReservations = async () => {
            try {
                // Replace with actual API call
                await new Promise((resolve) => setTimeout(resolve, 1000));
                let filteredReservations = [...mockAdminReservations];

                if (filters.parkingLot !== "all") {
                    filteredReservations = filteredReservations.filter(
                        (r) => r.parkingLot === filters.parkingLot
                    );
                }

                if (filters.status !== "all") {
                    filteredReservations = filteredReservations.filter(
                        (r) =>
                            r.status.toLowerCase() ===
                            filters.status.toLowerCase()
                    );
                }

                setReservations(filteredReservations);
            } catch (error) {
                console.error("Error fetching reservations:", error);
            } finally {
                setIsLoading(false);
            }
        };

        fetchReservations();
    }, [filters, user.id]);

    return (
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
            <div className="mb-8">
                <h1 className="text-2xl font-bold text-gray-900">
                    System Reservations
                </h1>
                <p className="mt-2 text-gray-600">
                    View and manage all parking reservations
                </p>
            </div>

            <div className="mb-6 grid grid-cols-1 md:grid-cols-3 gap-4">
                <div>
                    <label
                        htmlFor="parking-lot"
                        className="block text-sm font-medium text-gray-700"
                    >
                        Parking Lot
                    </label>
                    <select
                        id="parking-lot"
                        value={filters.parkingLot}
                        onChange={(e) =>
                            setFilters({
                                ...filters,
                                parkingLot: e.target.value,
                            })
                        }
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

                <div>
                    <label
                        htmlFor="status"
                        className="block text-sm font-medium text-gray-700"
                    >
                        Status
                    </label>
                    <select
                        id="status"
                        value={filters.status}
                        onChange={(e) =>
                            setFilters({ ...filters, status: e.target.value })
                        }
                        className="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm rounded-md"
                    >
                        <option value="all">All Statuses</option>
                        {statuses.map((status) => (
                            <option key={status} value={status}>
                                {status}
                            </option>
                        ))}
                    </select>
                </div>

                <div>
                    <label
                        htmlFor="date-range"
                        className="block text-sm font-medium text-gray-700"
                    >
                        Date Range
                    </label>
                    <select
                        id="date-range"
                        value={filters.dateRange}
                        onChange={(e) =>
                            setFilters({
                                ...filters,
                                dateRange: e.target.value,
                            })
                        }
                        className="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm rounded-md"
                    >
                        <option value="all">All Time</option>
                        <option value="today">Today</option>
                        <option value="week">This Week</option>
                        <option value="month">This Month</option>
                    </select>
                </div>
            </div>

            <ReservationList
                reservations={reservations}
                isLoading={isLoading}
            />
        </div>
    );
}
