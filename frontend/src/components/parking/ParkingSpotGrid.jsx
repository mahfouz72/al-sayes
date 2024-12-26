import { useState } from "react";

const spotTypes = {
    REGULAR: { color: "bg-blue-500", label: "Regular" },
    DISABLED: { color: "bg-yellow-500", label: "Disabled" },
    EV: { color: "bg-green-500", label: "EV Charging" },
};

const spotStatuses = {
    AVAILABLE: { color: "opacity-100", label: "Available" },
    OCCUPIED: { color: "opacity-30", label: "Occupied" },
    RESERVED: { color: "opacity-60", label: "Reserved" },
};

export default function ParkingSpotGrid({
    spots = [],
    onSpotSelect,
    isManager = false,
}) {
    const [selectedSpot, setSelectedSpot] = useState(null);

    const handleSpotClick = (spot) => {
        if (spot.currentStatus === "OCCUPIED" && !isManager) return;
        setSelectedSpot(spot.id === selectedSpot?.id ? null : spot);
        if (onSpotSelect) {
            onSpotSelect(spot);
        }
    };

    return (
        <div className="w-full max-w-4xl mx-auto">
            <div className="mb-4 flex gap-4 flex-wrap">
                {Object.entries(spotTypes).map(([key, value]) => (
                    <div key={key} className="flex items-center gap-2">
                        <div className={`w-4 h-4 ${value.color} rounded`}></div>
                        <span>{value.label}</span>
                    </div>
                ))}
            </div>

            <div className="grid grid-cols-5 gap-4">
                {spots.map((spot) => (
                    <button
                        key={spot.id}
                        onClick={() => handleSpotClick(spot)}
                        className={`
              ${spotTypes[spot.type]?.color || "bg-gray-500"}
              ${spotStatuses[spot.currentStatus]?.color || ""}
              p-4 rounded-lg text-white
              ${selectedSpot?.id === spot.id ? "ring-4 ring-indigo-500" : ""}
              transition-all duration-200
              ${
                  (spot.currentStatus === "OCCUPIED" ||
                      spot.currentStatus === "RESERVED") &&
                  !isManager
                      ? "cursor-not-allowed"
                      : "hover:scale-105"
              }
            `}
                    >
                        <div className="text-sm font-bold">S{spot.id}</div>
                        <div className="text-xs">
                            {spotStatuses[spot.currentStatus]?.label ||
                                spot.currentStatus}
                        </div>
                    </button>
                ))}
            </div>
        </div>
    );
}
