import { Fragment, useState, useEffect } from "react";
import { Dialog, Transition } from "@headlessui/react";
import { XMarkIcon } from "@heroicons/react/24/outline";
import ParkingSpotGrid from "./ParkingSpotGrid";
import { format, addHours, isAfter } from "date-fns";
import axios from "axios";

export default function ReservationModal({
    isOpen,
    onClose,
    parkingLot,
    parkingLotSpots,
    setParkingLotSpots,
}) {
    const [selectedSpot, setSelectedSpot] = useState(null);
    const [startTime, setStartTime] = useState("");
    const [endTime, setEndTime] = useState("");
    const [error, setError] = useState("");

    useEffect(() => {
        if (isOpen) {
            // Set start time to current time
            const now = new Date();
            const newStartTime = format(now, "yyyy-MM-dd'T'HH:mm");
            // Set end time to current time + 2 hours
            const newEndTime = format(addHours(now, 2), "yyyy-MM-dd'T'HH:mm");

            setSelectedSpot(null); // Reset selected spot
            setStartTime(newStartTime); // Set start time to now
            setEndTime(newEndTime); // Set end time to 2 hours from now
            setError(""); // Reset any previous error
        }
    }, [isOpen, setParkingLotSpots]);

    const fetchParkingLotSpots = async (start, end) => {
        try {
            const startStr = new Date(start).toISOString();
            const endStr = new Date(end).toISOString();
            const response = await axios.get(
                `http://localhost:8080/api/spots/${parkingLot.id}/get/available?start=${startStr}&end=${endStr}`,
                {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem(
                            "token"
                        )}`,
                    },
                }
            );
            setParkingLotSpots(response.data);
            console.log(response.data);
        } catch (error) {
            console.error("Error fetching parking spots:", error);
        }
    };

    const handleTimeChange = (start, end) => {
        setStartTime(start);
        setEndTime(end);
        setSelectedSpot(null);
        setParkingLotSpots([]);
        setError("");
        if (start && end) {
            const startDate = new Date(start);
            const endDate = new Date(end);
            if (isAfter(startDate, endDate)) {
                setError("End time must be after start time");
            } else fetchParkingLotSpots(start, end);
        }
    };

    const calculateTotal = () => {
        if (!startTime || !endTime) return 0;

        const start = new Date(startTime);
        const end = new Date(endTime);
        const hours = Math.ceil((end - start) / (1000 * 60 * 60));
        return (selectedSpot.cost * hours).toFixed(2);
    };

    const handleReserve = async () => {
        if (!startTime || !endTime || !selectedSpot || error) return;

        const reservationData = {
            driverId: null, // Leave this null; backend determines the driver
            lotId: parkingLot.id,
            spotId: selectedSpot.id,
            startTime: new Date(startTime).toISOString(),
            endTime: new Date(endTime).toISOString(),
            price: parseFloat(calculateTotal()),
            status: "PENDING", // Initial reservation status
            violationDuration: 0,
            penalty: 0,
        };

        try {
            const response = await axios.post(
                "http://localhost:8080/api/reservations/create",
                reservationData,
                {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem(
                            "token"
                        )}`,
                        "Content-Type": "application/json",
                    },
                }
            );

            if (response.status === 200) {
                // Reservation was successful
                console.log("Reservation successful:", reservationData);
                onClose(); // Close the modal
            } else {
                console.error("Unexpected response status:", response.status);
                setError("Failed to reserve the spot. Please try again.");
            }
        } catch (error) {
            // Handle errors during the request
            console.error("Error creating reservation:", error);
            setError(
                error.response?.data?.message ||
                    "Failed to reserve the spot. Please try again."
            );
        }
    };

    // // Handle reservation logic
    // console.log("Reserving spot:", {
    //     spot: selectedSpot,
    //     startTime,
    //     endTime,
    //     total: calculateTotal(),
    // });
    //     onClose();
    // };

    const minDateTime = format(new Date(), "yyyy-MM-dd'T'HH:mm");
    const maxDateTime = format(addHours(new Date(), 48), "yyyy-MM-dd'T'HH:mm");
    if (!parkingLot) return null;

    return (
        <Transition.Root show={isOpen} as={Fragment}>
            <Dialog as="div" className="relative z-10" onClose={onClose}>
                <Transition.Child
                    as={Fragment}
                    enter="ease-out duration-300"
                    enterFrom="opacity-0"
                    enterTo="opacity-100"
                    leave="ease-in duration-200"
                    leaveFrom="opacity-100"
                    leaveTo="opacity-0"
                >
                    <div className="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity" />
                </Transition.Child>

                <div className="fixed inset-0 z-10 overflow-y-auto">
                    <div className="flex min-h-full items-end justify-center p-4 text-center sm:items-center sm:p-0">
                        <Transition.Child
                            as={Fragment}
                            enter="ease-out duration-300"
                            enterFrom="opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95"
                            enterTo="opacity-100 translate-y-0 sm:scale-100"
                            leave="ease-in duration-200"
                            leaveFrom="opacity-100 translate-y-0 sm:scale-100"
                            leaveTo="opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95"
                        >
                            <Dialog.Panel className="relative transform overflow-hidden rounded-lg bg-white px-4 pb-4 pt-5 text-left shadow-xl transition-all sm:my-8 sm:w-full sm:max-w-4xl sm:p-6">
                                <div className="absolute right-0 top-0 pr-4 pt-4">
                                    <button
                                        type="button"
                                        className="rounded-md bg-white text-gray-400 hover:text-gray-500"
                                        onClick={onClose}
                                    >
                                        <span className="sr-only">Close</span>
                                        <XMarkIcon
                                            className="h-6 w-6"
                                            aria-hidden="true"
                                        />
                                    </button>
                                </div>
                                <div className="sm:flex sm:items-start">
                                    <div className="mt-3 text-center sm:mt-0 sm:text-left w-full">
                                        <Dialog.Title
                                            as="h3"
                                            className="text-lg font-semibold leading-6 text-gray-900"
                                        >
                                            Reserve a Spot at {parkingLot.name}
                                        </Dialog.Title>
                                        <div className="mt-6 grid grid-cols-1 gap-4 sm:grid-cols-2">
                                            <div>
                                                <label
                                                    htmlFor="start-time"
                                                    className="block text-sm font-medium text-gray-700"
                                                >
                                                    Start Time
                                                </label>
                                                <input
                                                    type="datetime-local"
                                                    id="start-time"
                                                    min={minDateTime}
                                                    value={startTime}
                                                    onChange={(e) =>
                                                        handleTimeChange(
                                                            e.target.value,
                                                            endTime
                                                        )
                                                    }
                                                    className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                                                />
                                            </div>
                                            <div>
                                                <label
                                                    htmlFor="end-time"
                                                    className="block text-sm font-medium text-gray-700"
                                                >
                                                    End Time
                                                </label>
                                                <input
                                                    type="datetime-local"
                                                    id="end-time"
                                                    min={
                                                        startTime || minDateTime
                                                    }
                                                    value={endTime}
                                                    onChange={(e) =>
                                                        handleTimeChange(
                                                            startTime,
                                                            e.target.value
                                                        )
                                                    }
                                                    className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                                                />
                                            </div>
                                        </div>
                                        {error && (
                                            <p className="mt-2 text-sm text-red-600">
                                                {error}
                                            </p>
                                        )}
                                        <div className="mt-6">
                                            <ParkingSpotGrid
                                                spots={parkingLotSpots}
                                                onSpotSelect={setSelectedSpot}
                                            />
                                        </div>
                                        {selectedSpot &&
                                            startTime &&
                                            endTime &&
                                            !error && (
                                                <div className="mt-4 bg-gray-50 p-4 rounded-lg">
                                                    <h4 className="font-medium text-gray-900">
                                                        Reservation Summary
                                                    </h4>
                                                    <p className="mt-2 text-sm text-gray-600">
                                                        Spot #{selectedSpot.id}
                                                    </p>
                                                    <p className="text-sm text-gray-600">
                                                        From:{" "}
                                                        {format(
                                                            new Date(startTime),
                                                            "MMM d, yyyy h:mm a"
                                                        )}
                                                    </p>
                                                    <p className="text-sm text-gray-600">
                                                        To:{" "}
                                                        {format(
                                                            new Date(endTime),
                                                            "MMM d, yyyy h:mm a"
                                                        )}
                                                    </p>
                                                    <p className="mt-2 text-lg font-semibold text-gray-900">
                                                        Total: $
                                                        {calculateTotal()}
                                                    </p>
                                                </div>
                                            )}
                                    </div>
                                </div>
                                <div className="mt-5 sm:mt-4 sm:flex sm:flex-row-reverse">
                                    <button
                                        type="button"
                                        className="inline-flex w-full justify-center rounded-md bg-blue-600 px-3 py-2 text-sm font-semibold text-white shadow-sm hover:bg-blue-500 sm:ml-3 sm:w-auto disabled:opacity-50 disabled:cursor-not-allowed"
                                        onClick={handleReserve}
                                        disabled={
                                            !selectedSpot ||
                                            !startTime ||
                                            !endTime ||
                                            error
                                        }
                                    >
                                        Confirm Reservation
                                    </button>
                                    <button
                                        type="button"
                                        className="mt-3 inline-flex w-full justify-center rounded-md bg-white px-3 py-2 text-sm font-semibold text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 hover:bg-gray-50 sm:mt-0 sm:w-auto"
                                        onClick={onClose}
                                    >
                                        Cancel
                                    </button>
                                </div>
                            </Dialog.Panel>
                        </Transition.Child>
                    </div>
                </div>
            </Dialog>
        </Transition.Root>
    );
}
