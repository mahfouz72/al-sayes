import { useState, useEffect } from "react";
import { MagnifyingGlassIcon } from "@heroicons/react/24/outline";
import { ChevronLeftIcon, ChevronRightIcon } from "@heroicons/react/20/solid";
import ParkingLotCard from "../parking/ParkingLotCard";
import ReservationModal from "../parking/ReservationModal";
import ParkingLotsMap from "../map/ParkingLotsMap";
import axios from "axios";

export default function DriverDashboard() {
    const [searchLocation, setSearchLocation] = useState("");
    const [selectedLot, setSelectedLot] = useState(null);
    const [isReservationModalOpen, setIsReservationModalOpen] = useState(false);
    const [parkingLots, setParkingLots] = useState([]);
    const [selectedLotSpots, setSelectedLotSpots] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [itemsPerPage] = useState(6);

    const totalPages = Math.ceil(parkingLots.length / itemsPerPage);
    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = currentPage * itemsPerPage;
    const paginatedParkingLots = parkingLots.slice(startIndex, endIndex);

    const fetchParkingLots = async () => {
        try {
            const token = localStorage.getItem("token");
            const response = await axios.get(
                "http://localhost:8080/api/lots/get/cards",
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
            );
            setParkingLots(response.data);
        } catch (error) {
            console.error("Error fetching parking lots:", error);
        }
    };

    useEffect(() => {
        fetchParkingLots();
    }, []);

    const onCloseReservationModal = () => {
        setIsReservationModalOpen(false);
        setSelectedLot(null);
        fetchParkingLots();
    };

    const handleReservation = (lot) => {
        setSelectedLot(lot);
        const fetchParkingLotSpots = async () => {
            try {
                const now = new Date();
                const end = new Date(now.getTime() + 2 * 60 * 60 * 1000);
                const start = now.toISOString();
                const endStr = end.toISOString();
                const response = await axios.get(
                    `http://localhost:8080/api/spots/${lot.id}/get/available?start=${start}&end=${endStr}`,
                    {
                        headers: {
                            Authorization: `Bearer ${localStorage.getItem(
                                "token"
                            )}`,
                        },
                    }
                );
                setSelectedLotSpots(response.data);
            } catch (error) {
                console.error("Error fetching parking spots:", error);
            }
        };

        fetchParkingLotSpots();
        setIsReservationModalOpen(true);
    };

    return (
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
            <div className="mb-8">
                <h1 className="text-3xl font-bold text-gray-900">
                    Find Parking
                </h1>
                <p className="mt-2 text-gray-600">
                    Search and reserve parking spots near you
                </p>
            </div>

            <div className="mb-8">
                <ParkingLotsMap
                    parkingLots={parkingLots}
                    onLotSelect={handleReservation}
                />
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {paginatedParkingLots.map((lot) => (
                    <ParkingLotCard
                        key={lot.id}
                        lot={lot}
                        onReserve={handleReservation}
                    />
                ))}
            </div>

            <div className="flex items-center justify-between px-4 py-3 bg-white border-t border-gray-200 sm:px-6">
                <div className="hidden sm:flex sm:flex-1 sm:items-center sm:justify-between">
                    <p className="text-sm text-gray-700">
                        Showing{" "}
                        <span className="font-medium">{startIndex + 1}</span> to{" "}
                        <span className="font-medium">
                            {Math.min(endIndex, parkingLots.length)}
                        </span>{" "}
                        of{" "}
                        <span className="font-medium">
                            {parkingLots.length}
                        </span>{" "}
                        results
                    </p>
                    <nav className="relative z-0 inline-flex rounded-md shadow-sm -space-x-px">
                        <button
                            onClick={() =>
                                setCurrentPage((page) => Math.max(page - 1, 1))
                            }
                            disabled={currentPage === 1}
                            className="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50"
                        >
                            <span className="sr-only">Previous</span>
                            <ChevronLeftIcon className="h-5 w-5" />
                        </button>
                        {[...Array(totalPages)].map((_, i) => (
                            <button
                                key={i + 1}
                                onClick={() => setCurrentPage(i + 1)}
                                className={`relative inline-flex items-center px-4 py-2 border text-sm font-medium ${
                                    currentPage === i + 1
                                        ? "z-10 bg-blue-50 border-blue-500 text-blue-600"
                                        : "bg-white border-gray-300 text-gray-500 hover:bg-gray-50"
                                }`}
                            >
                                {i + 1}
                            </button>
                        ))}
                        <button
                            onClick={() =>
                                setCurrentPage((page) =>
                                    Math.min(page + 1, totalPages)
                                )
                            }
                            disabled={currentPage === totalPages}
                            className="relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50"
                        >
                            <span className="sr-only">Next</span>
                            <ChevronRightIcon className="h-5 w-5" />
                        </button>
                    </nav>
                </div>
            </div>

            <ReservationModal
                isOpen={isReservationModalOpen}
                onClose={onCloseReservationModal}
                parkingLot={selectedLot}
                parkingLotSpots={selectedLotSpots}
                setParkingLotSpots={setSelectedLotSpots}
            />
        </div>
    );
}
