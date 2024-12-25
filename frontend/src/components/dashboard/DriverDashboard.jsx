import { useState, useEffect } from "react";
import { MapPinIcon, MagnifyingGlassIcon } from "@heroicons/react/24/outline";
import ParkingLotCard from "../parking/ParkingLotCard";
import ReservationModal from "../parking/ReservationModal";
import axios from "axios";
import { set } from "date-fns";

// const mockParkingLots = [
//   {
//     id: 1,
//     name: 'Downtown Parking',
//     location: '123 Main St, Downtown',
//     available: 45,
//     total: 100,
//     pricePerHour: 5,
//     spotTypes: {
//       regular: 80,
//       disabled: 10,
//       ev: 10
//     },
//     spots: Array(100).fill(null).map((_, i) => ({
//       id: i + 1,
//       number: `A${i + 1}`,
//       type: i < 80 ? 'REGULAR' : i < 90 ? 'DISABLED' : 'EV',
//       status: Math.random() > 0.5 ? 'AVAILABLE' : 'OCCUPIED'
//     }))
//   },
//   {
//     id: 2,
//     name: 'Mall Parking',
//     location: '456 Shopping Ave',
//     available: 20,
//     total: 150,
//     pricePerHour: 3,
//     spotTypes: {
//       regular: 120,
//       disabled: 20,
//       ev: 10
//     },
//     spots: Array(150).fill(null).map((_, i) => ({
//       id: i + 1,
//       number: `B${i + 1}`,
//       type: i < 120 ? 'REGULAR' : i < 140 ? 'DISABLED' : 'EV',
//       status: Math.random() > 0.5 ? 'AVAILABLE' : 'OCCUPIED'
//     }))
//   }
// ]

export default function DriverDashboard() {
    const [searchLocation, setSearchLocation] = useState("");
    const [selectedLot, setSelectedLot] = useState(null);
    const [isReservationModalOpen, setIsReservationModalOpen] = useState(false);
    const [parkingLots, setParkingLots] = useState([]);
    const [selectedLotSpots, setSelectedLotSpots] = useState([]);

    useEffect(() => {
        const fetchParkingLots = async () => {
            try {
                const response = await axios.get(
                    "http://localhost:8080/api/lots/get/cards"
                ); // Replace with your actual API endpoint
                setParkingLots(response.data);
            } catch (error) {
                console.error("Error fetching parking lots:", error);
            }
        };

        fetchParkingLots();
    }, []);

    const handleReservation = (lot) => {
        setSelectedLot(lot);
        const fetchParkingLotSpots = async () => {
            try {
                const now = new Date();
                const end = new Date(now.getTime() + 2 * 60 * 60 * 1000);
                const start = now.toISOString();
                const endStr = end.toISOString();
                const response = await axios.get(
                    `http://localhost:8080/api/spots/${lot.id}/get/available?start=${start}&end=${endStr}`
                );
                setSelectedLotSpots(response.data);
                console.log(response.data);
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
                onClose={() => setIsReservationModalOpen(false)}
                parkingLot={selectedLot}
                parkingLotSpots={selectedLotSpots}
                setParkingLotSpots={setSelectedLotSpots}
            />
        </div>
    );
}
