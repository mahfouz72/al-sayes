import React, { useEffect, useState } from "react";
import useAuthStore from "../../store/authStore";
import ProfileHeader from "./ProfileHeader";
import ProfileCard from "./ProfileCard";
import axios from "axios";

const mockUser = {
    username: "John Doe",
    email: "john@gmail.com",
    role: "admin",
    licensePlate: "ABC123",
    paymentMethod: "Visa",
};

export default function UserProfile() {
    const [userDetails, setUserDetails] = useState({});
    const fetchUser = async () => {
        try {
            const response = await axios.get(
                `http://localhost:8080/api/users/`,
                {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem(
                            "token"
                        )}`,
                    },
                }
            );
            setUserDetails(response.data);
            console.log("User details:", response.data);
        } catch (error) {
            console.error("Error fetching user details:", error);
        }
    };
    useEffect(() => {
        fetchUser();
    }, []);

    return (
        <div className="max-w-3xl mx-auto px-4 py-8 sm:px-6 lg:px-8">
            <div className="space-y-8">
                <ProfileHeader user={userDetails} role={userDetails.role} />
                <ProfileCard user={userDetails} role={userDetails.role} />
            </div>
        </div>
    );
}
