import React from "react";

export default function ProfileHeader({ user, role }) {
    return (
        <div className="flex items-center space-x-6">
            <div className="h-24 w-24 rounded-full bg-gradient-to-br from-blue-500 to-purple-600 flex items-center justify-center text-white text-3xl font-semibold shadow-lg">
                {user?.username?.[0]?.toUpperCase() || "U"}
            </div>
            <div>
                <h1 className="text-2xl font-bold text-gray-900">
                    {user?.username}
                </h1>
                <span className="inline-flex items-center px-3 py-1 rounded-full text-sm font-medium bg-blue-100 text-blue-800 capitalize">
                    {role}
                </span>
            </div>
        </div>
    );
}
