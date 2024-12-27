import { useEffect, useState } from "react";
import {
    BarChart,
    Bar,
    XAxis,
    YAxis,
    CartesianGrid,
    Tooltip,
    Legend,
} from "recharts";
import ParkingSpotGrid from "../parking/ParkingSpotGrid";
import LotAPI from "../../apis/LotAPI";
// import SpotAPI from "../../apis/SpotAPI";
import { Client } from "@stomp/stompjs";
import { getUserToken } from "../../apis/config";

export default function ManagerDashboard() {
    const [parkingLots, setParkingLots] = useState([]);
    const [selectedLot, setSelectedLot] = useState(parkingLots[0]);
    const [selectedLotSpots, setSelectedLotSpots] = useState([]); // Store spots for selected lot
    const [showSpotGrid, setShowSpotGrid] = useState(false);
    // Fetch all parking lots when the page first renders
    useEffect(() => {
        LotAPI.getParkingLots()
            .then((response) => {
                setParkingLots(response);
                if (response.length > 0) {
                    setSelectedLot(response[0]); // Select the first lot by default
                    console.log("Selected lot: ", response[0]);
                }
            })
            .catch((error) => {
                console.error(
                    "There was an error fetching the parking lots data:",
                    error
                );
            });
    }, []);

    // Fetch parking spots when selected lot changes
    useEffect(() => {
        if (selectedLot) {
            const client = new Client({
                brokerURL: "ws://localhost:8080/ws",
                headers: {
                    Authorization: `Bearer ${getUserToken()}`,
                },
                debug: (str) => console.log(str),
                onConnect: () => {
                    console.log(
                        "Connected to WebSocket in selectedLot.id = " +
                            selectedLot.id
                    );
                    // Subscribe to the topic where the backend will send the parking spots
                    client.subscribe(
                        `/topic/spots/${selectedLot.id}`,
                        (message) => {
                            const spots = JSON.parse(message.body); // Parse the spots from the response
                            console.log("Received spots:", spots);
                            setSelectedLotSpots(spots); // Update the state with the parking spots
                        }
                    );

                    // Send a message to the backend (with selected lot ID) to request parking spots
                    client.publish({
                        destination: `/app/spots/${selectedLot.id}`, // Send request to backend
                    });
                },
                onStompError: (frame) => {
                    console.error("WebSocket error:", frame);
                    console.error(
                        "Broker reported error: ",
                        frame.headers["message"]
                    );
                    console.error("Additional details: ", frame.body);
                },
                onDisconnect: () => {
                    console.log("WebSocket connection closed");
                },
            });
            client.activate();

            // Cleanup on component unmount
            return () => {
                if (client.active) {
                    client.deactivate();
                    console.log("WebSocket connection deactivated");
                }
            };
        }
    }, [selectedLot]);

    const handleSpotUpdate = (spot) => {
        console.log("Updating spot status:", spot);
        // Update spot status logic here
    };

    const data = parkingLots.map((lot) => ({
        name: lot.name,
        occupancy: lot.occupancyRate,
        revenue: lot.revenue,
    }));

    if (parkingLots === null) {
        return <div>Loading...</div>; // Show loading state while the data is being fetched
    }

    if (parkingLots.length === 0) {
        return <div>No parking lots available.</div>; // Show message if the list is empty
    }

    return (
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
            <div className="mb-8">
                <h1 className="text-3xl font-bold text-gray-900">
                    Parking Lot Management
                </h1>
                <p className="mt-2 text-gray-600">
                    Monitor and manage your parking facilities
                </p>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
                <div className="bg-white rounded-lg shadow p-6">
                    <h3 className="text-lg font-semibold text-gray-900">
                        Total Revenue
                    </h3>
                    <p className="mt-2 text-3xl font-bold text-blue-600">
                        $
                        {parkingLots
                            .reduce((sum, lot) => sum + lot.revenue, 0)
                            .toFixed(2)}
                    </p>
                </div>
                <div className="bg-white rounded-lg shadow p-6">
                    <h3 className="text-lg font-semibold text-gray-900">
                        Average Occupancy
                    </h3>
                    <p className="mt-2 text-3xl font-bold text-green-600">
                        {Math.round(
                            parkingLots.reduce(
                                (sum, lot) => sum + lot.occupancyRate,
                                0
                            ) / parkingLots.length
                        ).toFixed(2)}
                        %
                    </p>
                </div>
                <div className="bg-white rounded-lg shadow p-6">
                    <h3 className="text-lg font-semibold text-gray-900">
                        Total Violations
                    </h3>
                    <p className="mt-2 text-3xl font-bold text-red-600">
                        {parkingLots.reduce(
                            (sum, lot) => sum + lot.violations,
                            0
                        )}
                    </p>
                </div>
            </div>

            <div className="bg-white rounded-lg shadow mb-8">
                <div className="p-6">
                    <h2 className="text-xl font-semibold text-gray-900 mb-4">
                        Performance Overview
                    </h2>
                    <div className="w-full overflow-x-auto">
                        <BarChart width={1100} height={500} data={data}>
                            <CartesianGrid strokeDasharray="3 3" />
                            <XAxis
                                dataKey="name"
                                angle={50}
                                textAnchor="start"
                                interval={0}
                                height={200}
                            />
                            <YAxis />
                            <Tooltip />
                            <Legend />
                            <Bar
                                dataKey="occupancy"
                                fill="#8884d8"
                                name="Occupancy %"
                            />
                            <Bar
                                dataKey="revenue"
                                fill="#82ca9d"
                                name="Revenue ($)"
                            />
                        </BarChart>
                    </div>
                </div>
            </div>

            <div className="bg-white rounded-lg shadow">
                <div className="p-6">
                    <div className="flex justify-between items-center mb-6">
                        <h2 className="text-xl font-semibold text-gray-900">
                            Parking Lots
                        </h2>
                        <select
                            value={selectedLot.id}
                            onChange={(e) =>
                                setSelectedLot(
                                    parkingLots.find(
                                        (lot) =>
                                            lot.id === Number(e.target.value)
                                    )
                                )
                            }
                            className="rounded-md border-gray-300 py-2 pl-3 pr-10 text-base focus:border-blue-500 focus:outline-none focus:ring-blue-500"
                        >
                            {parkingLots.map((lot) => (
                                <option key={lot.id} value={lot.id}>
                                    {lot.name}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="space-y-6">
                        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                            <div className="bg-gray-50 rounded-lg p-4">
                                <h4 className="text-sm font-medium text-gray-500">
                                    Location
                                </h4>
                                <p className="mt-1 text-lg font-semibold">
                                    {selectedLot.location}
                                </p>
                            </div>
                            <div className="bg-gray-50 rounded-lg p-4">
                                <h4 className="text-sm font-medium text-gray-500">
                                    Current Occupancy
                                </h4>
                                <p className="mt-1 text-lg font-semibold">
                                    {selectedLot.occupancyRate.toFixed(2)}%
                                </p>
                            </div>
                            <div className="bg-gray-50 rounded-lg p-4">
                                <h4 className="text-sm font-medium text-gray-500">
                                    {" "}
                                    Revenue
                                </h4>
                                <p className="mt-1 text-lg font-semibold">
                                    ${selectedLot.revenue.toFixed(2)}
                                </p>
                            </div>
                        </div>

                        <div>
                            <button
                                onClick={() => setShowSpotGrid(!showSpotGrid)}
                                className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors"
                            >
                                {showSpotGrid
                                    ? "Hide Spot Grid"
                                    : "Show Spot Grid"}
                            </button>

                            {showSpotGrid && (
                                <div className="mt-4">
                                    <ParkingSpotGrid
                                        spots={selectedLotSpots}
                                        onSpotSelect={handleSpotUpdate}
                                        isManager={true}
                                    />
                                </div>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
