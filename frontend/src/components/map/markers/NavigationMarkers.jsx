import L from "leaflet";

export const createUserIcon = () =>
    L.divIcon({
        html: `<div class="user-marker">
          <div class="user-marker-dot"></div>
          <div class="user-marker-pulse"></div>
        </div>`,
        className: "",
        iconSize: [20, 20],
        iconAnchor: [10, 10],
    });

export const createDestinationIcon = () =>
    L.divIcon({
        html: `<div class="destination-marker">
          <div class="destination-marker-dot"></div>
          <div class="destination-marker-pulse"></div>
        </div>`,
        className: "",
        iconSize: [20, 20],
        iconAnchor: [10, 10],
    });
