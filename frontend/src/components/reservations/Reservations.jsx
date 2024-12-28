import { useState, useEffect } from "react";
import ReservationList from "./ReservationList";
import axios from "axios";

export default function Reservations() {
    const [reservations, setReservations] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [parkingLots, setParkingLots] = useState([]);
    const [statuses, setStatuses] = useState([]);
    const [filters, setFilters] = useState({
        parkingLot: "all",
        status: "all",
        dateRange: "all",
    });
    const [sortConfig, setSortConfig] = useState({
        key: null,
        direction: "asc",
    });

    const endPointMapper = {
        admin: "admin",
        driver: "drivers",
        manager: "lot-manager",
    };

    const h1Mapper = {
        admin: "System Reservations",
        driver: "My Reservations",
        manager: "My Parking Lots Reservations",
    };

    const pMapper = {
        admin: "View and manage all parking reservations",
        driver: "View and manage your parking reservations",
        manager: "View and manage your parking lots reservations",
    };

    const user = localStorage.getItem("username");
    const role = localStorage.getItem("role");

    const sortData = (data, key, direction) => {
        return [...data].sort((a, b) => {
            if (key === "startTime") {
                return direction === "asc"
                    ? new Date(a[key]) - new Date(b[key])
                    : new Date(b[key]) - new Date(a[key]);
            }
            if (key === "total") {
                return direction === "asc"
                    ? parseFloat(a[key]) - parseFloat(b[key])
                    : parseFloat(b[key]) - parseFloat(a[key]);
            }
            if (key === "spotNumber") {
                const aNum = parseInt(a[key].substring(1));
                const bNum = parseInt(b[key].substring(1));
                return direction === "asc" ? aNum - bNum : bNum - aNum;
            }
            if (a[key] < b[key]) return direction === "asc" ? -1 : 1;
            if (a[key] > b[key]) return direction === "asc" ? 1 : -1;
            return 0;
        });
    };

    useEffect(() => {
        const fetchReservations = async () => {
            try {
                const token = localStorage.getItem("token");
                const headers = {
                    Authorization: `Bearer ${token}`,
                };
                const response = await axios.get(
                    `http://localhost:8080/${endPointMapper[role]}/reservations`,
                    { headers }
                );

                const data = response.data;

                const reservationsWithId = data.map((reservation, index) => ({
                    id: index + 1,
                    ...reservation,
                }));
                const lots = new Set();
                const statuses = new Set();
                reservationsWithId.forEach((r) => {
                    lots.add(r.parkingLot);
                    statuses.add(r.status);
                });
                setParkingLots([...lots]);
                setStatuses([...statuses]);

                let filteredReservations = [...reservationsWithId];

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

                if (filters.dateRange !== "all") {
                    const now = new Date();
                    let start = new Date();
                    let end = new Date();
                    if (filters.dateRange === "today") {
                        start = new Date(
                            now.getFullYear(),
                            now.getMonth(),
                            now.getDate()
                        );
                    }
                    if (filters.dateRange === "week") {
                        start = new Date(
                            now.getFullYear(),
                            now.getMonth(),
                            now.getDate() - now.getDay()
                        );
                    }
                    if (filters.dateRange === "month") {
                        start = new Date(now.getFullYear(), now.getMonth(), 1);
                    }
                    filteredReservations = filteredReservations.filter((r) => {
                        const reservationDate = new Date(r.startTime);
                        return (
                            reservationDate >= start && reservationDate <= end
                        );
                    });
                }

                if (sortConfig.key) {
                    filteredReservations = sortData(
                        filteredReservations,
                        sortConfig.key,
                        sortConfig.direction
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
    }, [filters, sortConfig, user.id]);

    const handleSort = (key) => {
        setSortConfig((prevConfig) => ({
            key,
            direction:
                prevConfig.key === key && prevConfig.direction === "asc"
                    ? "desc"
                    : "asc",
        }));
    };

    return (
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
            <div className="mb-8">
                <h1 className="text-2xl font-bold text-gray-900">
                    {h1Mapper[role]}
                </h1>
                <p className="mt-2 text-gray-600">{pMapper[role]}</p>
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
                sortConfig={sortConfig}
                onSort={handleSort}
            />
        </div>
    );
}
