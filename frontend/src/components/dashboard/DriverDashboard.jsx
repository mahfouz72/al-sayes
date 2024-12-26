import { useState, useEffect } from "react";
import { MagnifyingGlassIcon } from "@heroicons/react/24/outline";
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

            {/* <div className="mb-8">
                <div className="max-w-xl relative">
                    <MagnifyingGlassIcon className="h-5 w-5 absolute left-3 top-3 text-gray-400" />
                    <input
                        type="text"
                        placeholder="Search by location..."
                        className="w-full pl-10 pr-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                        value={searchLocation}
                        onChange={(e) => setSearchLocation(e.target.value)}
                    />
                </div>
            </div> */}

            <div className="mb-8">
                <ParkingLotsMap
                    parkingLots={parkingLots}
                    onLotSelect={handleReservation}
                />
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {parkingLots.map((lot) => (
                    <ParkingLotCard
                        key={lot.id}
                        lot={lot}
                        onReserve={handleReservation}
                    />
                ))}
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
