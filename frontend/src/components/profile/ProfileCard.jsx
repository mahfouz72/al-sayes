import React from "react";
import { CreditCard, Mail, User as UserIcon, Car, Wallet } from "lucide-react";

export default function ProfileCard({ user, role }) {
    return (
        <div className="bg-white rounded-xl shadow-md overflow-hidden">
            <div className="p-8">
                <div className="space-y-6">
                    {/* Username */}
                    <div className="flex items-center space-x-4">
                        <div className="flex-shrink-0">
                            <UserIcon className="h-6 w-6 text-blue-500" />
                        </div>
                        <div>
                            <p className="text-sm font-medium text-gray-500">
                                Username
                            </p>
                            <p className="mt-1 text-lg font-semibold text-gray-900">
                                {user?.username}
                            </p>
                        </div>
                    </div>

                    {/* Email */}
                    <div className="flex items-center space-x-4">
                        <div className="flex-shrink-0">
                            <Mail className="h-6 w-6 text-blue-500" />
                        </div>
                        <div>
                            <p className="text-sm font-medium text-gray-500">
                                Email
                            </p>
                            <p className="mt-1 text-lg font-semibold text-gray-900">
                                {user?.email}
                            </p>
                        </div>
                    </div>

                    {/* License Plate - Only for drivers */}
                    {role === "driver" && user?.licensePlate && (
                        <div className="flex items-center space-x-4">
                            <div className="flex-shrink-0">
                                <Car className="h-6 w-6 text-blue-500" />
                            </div>
                            <div>
                                <p className="text-sm font-medium text-gray-500">
                                    License Plate
                                </p>
                                <p className="mt-1 text-lg font-semibold text-gray-900">
                                    {user.licensePlate}
                                </p>
                            </div>
                        </div>
                    )}

                    {/* Payment Method - If exists */}
                    {user?.paymentMethod && (
                        <div className="flex items-center space-x-4">
                            <div className="flex-shrink-0">
                                {user.paymentMethod === "visa" ? (
                                    <CreditCard className="h-6 w-6 text-blue-500" />
                                ) : (
                                    <Wallet className="h-6 w-6 text-blue-500" />
                                )}
                            </div>
                            <div>
                                <p className="text-sm font-medium text-gray-500">
                                    Payment Method
                                </p>
                                <p className="mt-1 text-lg font-semibold text-gray-900 capitalize">
                                    {user.paymentMethod}
                                </p>
                            </div>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}
