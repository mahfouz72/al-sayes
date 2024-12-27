import React from "react";

export default function Avatar({ username, size = "lg" }) {
    const sizes = {
        sm: "h-9 w-9 text-lg",
        lg: "h-24 w-24 text-3xl",
    };

    return (
        <div
            className={`${sizes[size]} rounded-full bg-gradient-to-br from-blue-600 to-blue-700 flex items-center justify-center text-white font-semibold shadow-lg`}
        >
            {username?.[0]?.toUpperCase() || "U"}
        </div>
    );
}
