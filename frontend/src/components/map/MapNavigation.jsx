import { useEffect, useRef, useState } from "react";
import "leaflet/dist/leaflet.css";
import "./MapNavigation.css";
import "./styles/markers.css";
import {
    createUserIcon,
    createDestinationIcon,
} from "./markers/NavigationMarkers";
import { EyeIcon, EyeOffIcon } from "lucide-react";

export default function MapNavigation({ destination, userLocation }) {
    const mapRef = useRef(null);
    const mapInstanceRef = useRef(null);
    const routingControlRef = useRef(null);
    const markersRef = useRef({ user: null, destination: null });
    const [showInstructions, setShowInstructions] = useState(true);

    useEffect(() => {
        console.log(userLocation, destination);
        const initMap = async () => {
            const L = await import("leaflet");
            const LRM = await import("leaflet-routing-machine");

            // Ensure mapRef is valid
            if (!mapRef.current) {
                console.error("Map container not available.");
                return;
            }

            try {
                if (!mapInstanceRef.current) {
                    mapInstanceRef.current = L.map(mapRef.current).setView(
                        userLocation,
                        13
                    );

                    L.tileLayer(
                        "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png",
                        {
                            attribution: "© OpenStreetMap contributors",
                        }
                    ).addTo(mapInstanceRef.current);
                }
            } catch (error) {
                console.error("Error initializing map:", error);
                return;
            }

            if (routingControlRef.current) {
                routingControlRef.current.remove();
            }
            // Update or create user marker
            if (userLocation) {
                if (markersRef.current.user) {
                    markersRef.current.user.setLatLng([
                        userLocation.lat,
                        userLocation.lng,
                    ]);
                } else {
                    markersRef.current.user = L.marker(
                        [userLocation.lat, userLocation.lng],
                        {
                            icon: createUserIcon(),
                            zIndexOffset: 1000,
                        }
                    )
                        .addTo(mapInstanceRef.current)
                        .bindPopup("You are here");
                }
            }

            if (userLocation && destination) {
                if (L.Routing && L.Routing.control) {
                    routingControlRef.current = L.Routing.control({
                        waypoints: [
                            L.latLng(userLocation.lat, userLocation.lng),
                            L.latLng(
                                destination.latitude,
                                destination.longitude
                            ),
                        ],
                        routeWhileDragging: true,
                        showAlternatives: true,
                        addWaypoints: false,
                        scrollWheelZoom: false,
                        lineOptions: {
                            styles: [{ color: "#6366f1", weight: 6 }],
                        },
                        router: L.Routing.osrmv1({
                            serviceUrl:
                                "http://router.project-osrm.org/route/v1",
                            profile: "driving",
                        }),
                        containerClassName: "routing-container",
                    }).addTo(mapInstanceRef.current);

                    const container =
                        document.querySelector(".routing-container");
                    if (container) {
                        container.style.display = showInstructions
                            ? "block"
                            : "none";
                    }

                    const bounds = L.latLngBounds([userLocation, destination]);
                    mapInstanceRef.current.fitBounds(bounds);
                } else {
                    console.error(
                        "Leaflet Routing Machine is not properly loaded."
                    );
                }
                // Update or create destination marker
                if (destination) {
                    if (markersRef.current.destination) {
                        markersRef.current.destination.setLatLng([
                            destination.latitude,
                            destination.longitude,
                        ]);
                    } else {
                        markersRef.current.destination = L.marker(
                            [destination.latitude, destination.longitude],
                            {
                                icon: createDestinationIcon(),
                            }
                        )
                            .addTo(mapInstanceRef.current)
                            .bindPopup(destination.name);
                    }
                }
            }
        };

        initMap();

        return () => {
            if (mapInstanceRef.current) {
                mapInstanceRef.current.remove();
                mapInstanceRef.current = null;
            }
        };
    }, [destination, userLocation]);

    useEffect(() => {
        const container = document.querySelector(".routing-container");
        if (container) {
            container.style.display = showInstructions ? "block" : "none";
        }
    }, [showInstructions]);

    return (
        <div className="relative w-full h-[400px] rounded-lg overflow-hidden shadow-lg">
            <div ref={mapRef} className="w-full h-full" />

            <button
                onClick={() => setShowInstructions(!showInstructions)}
                className="absolute top-4 right-4 z-[1000] bg-white p-2 rounded-full shadow-md hover:bg-gray-50 transition-colors"
                title={
                    showInstructions ? "Hide Instructions" : "Show Instructions"
                }
            >
                {showInstructions ? (
                    <EyeIcon className="w-5 h-5 text-gray-600" />
                ) : (
                    <EyeOffIcon className="w-5 h-5 text-gray-600" />
                )}
            </button>

            {(!userLocation || !destination) && (
                <div className="absolute inset-0 bg-gray-100 flex items-center justify-center">
                    <p className="text-gray-500">Loading map...</p>
                </div>
            )}
        </div>
    );
}
