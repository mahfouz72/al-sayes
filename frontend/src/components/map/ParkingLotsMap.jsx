import { useEffect, useRef } from "react";
import L from "leaflet";
import "leaflet/dist/leaflet.css";
import "./styles/map.css";
import { createParkingIcon } from "./ParkingMarker";
import { createPopupContent } from "./ParkingPopup";
import useGeolocation from "../../hooks/useGeolocation";
import { createUserIcon } from "./markers/NavigationMarkers";

export default function ParkingLotsMap({ parkingLots, onLotSelect }) {
    const mapRef = useRef(null);
    const mapInstanceRef = useRef(null);
    const markersRef = useRef([]);
    const { location: userLocation, error: locationError } = useGeolocation();

    useEffect(() => {
        if (!mapInstanceRef.current) {
            mapInstanceRef.current = L.map(mapRef.current).setView(
                [40.7128, -74.006],
                13
            );

            L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
                attribution: "© OpenStreetMap contributors",
            }).addTo(mapInstanceRef.current);
        }

        // Clear existing markers
        markersRef.current.forEach((marker) => marker.remove());
        markersRef.current = [];

        // Add markers for each parking lot
        parkingLots.forEach((lot) => {
            const marker = L.marker([lot.latitude, lot.longitude], {
                icon: createParkingIcon(),
            })
                .addTo(mapInstanceRef.current)
                .bindPopup(createPopupContent(lot));

            marker.on("click", () => {
                marker.openPopup();
            });

            markersRef.current.push(marker);
        });

        // Add user location marker
        if (userLocation) {
            const userMarker = L.marker([userLocation.lat, userLocation.lng], {
                icon: createUserIcon(),
                zIndexOffset: 1000,
            }).addTo(mapInstanceRef.current);
            markersRef.current.push(userMarker);
        }

        // Add event listener for lot selection
        const handleLotSelect = (e) => {
            const lot = parkingLots.find((l) => l.id === e.detail);
            if (lot) onLotSelect(lot);
        };
        document.addEventListener("selectLot", handleLotSelect);

        // Fit bounds to show all markers
        if (parkingLots.length > 0) {
            const bounds = L.latLngBounds(
                parkingLots
                    .map((lot) => [lot.latitude, lot.longitude])
                    // Add user location to bounds
                    .concat(
                        userLocation
                            ? [[userLocation.lat, userLocation.lng]]
                            : []
                    )
            );
            mapInstanceRef.current.fitBounds(bounds, { padding: [50, 50] });
        }

        return () => {
            document.removeEventListener("selectLot", handleLotSelect);
        };
    }, [parkingLots, onLotSelect]);

    return (
        <div className="map-container">
            <div
                ref={mapRef}
                className="w-full h-[400px] rounded-lg overflow-hidden shadow-lg"
            />
        </div>
    );
}
