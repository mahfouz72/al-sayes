import { useState } from "react";
import { format } from "date-fns";
import { ChevronLeftIcon, ChevronRightIcon } from "@heroicons/react/24/outline";
import useAuthStore from "../../store/authStore";

const ITEMS_PER_PAGE = 10;

export default function ReservationList({
    reservations = [],
    isLoading = false,
}) {
    const [currentPage, setCurrentPage] = useState(1);
    const { role } = useAuthStore();

    const totalPages = Math.ceil(reservations.length / ITEMS_PER_PAGE);
    const startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
    const endIndex = startIndex + ITEMS_PER_PAGE;
    const currentReservations = reservations.slice(startIndex, endIndex);

    const getStatusColor = (status) => {
        switch (status.toLowerCase()) {
            case "active":
                return "bg-green-100 text-green-800";
            case "completed":
                return "bg-gray-100 text-gray-800";
            case "cancelled":
                return "bg-red-100 text-red-800";
            default:
                return "bg-blue-100 text-blue-800";
        }
    };

    if (isLoading) {
        return (
            <div className="flex justify-center items-center h-64">
                <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
            </div>
        );
    }

    return (
        <div>
            <div className="overflow-x-auto">
                <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-gray-50">
                        <tr>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                ID
                            </th>
                            {(role === "manager" || role === "admin") && (
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                    Driver
                                </th>
                            )}
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Parking Lot
                            </th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Spot
                            </th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Start Time
                            </th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Duration
                            </th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Status
                            </th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Total
                            </th>
                        </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                        {currentReservations.map((reservation) => (
                            <tr key={reservation.id}>
                                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                                    #{reservation.id}
                                </td>
                                {(role === "manager" || role === "admin") && (
                                    <td className="px-6 py-4 whitespace-nowrap">
                                        <div className="text-sm font-medium text-gray-900">
                                            {reservation.driver?.name}
                                        </div>
                                        <div className="text-sm text-gray-500">
                                            {reservation.driver?.email}
                                        </div>
                                    </td>
                                )}
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                    {reservation.parkingLot}
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                    {reservation.spotNumber}
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                    {format(
                                        new Date(reservation.startTime),
                                        "MMM d, yyyy h:mm a"
                                    )}
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                    {reservation.duration} hours
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap">
                                    <span
                                        className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${getStatusColor(
                                            reservation.status
                                        )}`}
                                    >
                                        {reservation.status}
                                    </span>
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                    ${reservation.total}
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
            {/* Pagination section remains the same */} {/* Pagination */}
            <div className="flex items-center justify-between px-4 py-3 bg-white border-t border-gray-200 sm:px-6">
                <div className="flex justify-between flex-1 sm:hidden">
                    <button
                        onClick={() =>
                            setCurrentPage((page) => Math.max(page - 1, 1))
                        }
                        disabled={currentPage === 1}
                        className="relative inline-flex items-center px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-md hover:bg-gray-50"
                    >
                        Previous
                    </button>
                    <button
                        onClick={() =>
                            setCurrentPage((page) =>
                                Math.min(page + 1, totalPages)
                            )
                        }
                        disabled={currentPage === totalPages}
                        className="relative ml-3 inline-flex items-center px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-md hover:bg-gray-50"
                    >
                        Next
                    </button>
                </div>
                <div className="hidden sm:flex sm:flex-1 sm:items-center sm:justify-between">
                    <div>
                        <p className="text-sm text-gray-700">
                            Showing{" "}
                            <span className="font-medium">
                                {startIndex + 1}
                            </span>{" "}
                            to{" "}
                            <span className="font-medium">
                                {Math.min(endIndex, reservations.length)}
                            </span>{" "}
                            of{" "}
                            <span className="font-medium">
                                {reservations.length}
                            </span>{" "}
                            results
                        </p>
                    </div>
                    <div>
                        <nav className="relative z-0 inline-flex rounded-md shadow-sm -space-x-px">
                            <button
                                onClick={() =>
                                    setCurrentPage((page) =>
                                        Math.max(page - 1, 1)
                                    )
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
            </div>
        </div>
    );
}
