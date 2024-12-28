import { CreditCard, Mail, User as UserIcon, Car, Wallet } from "lucide-react";
import ProfileField from "./ProfileField";

export default function ProfileCard({ user, role }) {
    return (
        <div className="bg-white rounded-xl shadow-md overflow-hidden">
            <div className="p-8">
                <div className="space-y-6">
                    <ProfileField
                        icon={UserIcon}
                        label="Username"
                        value={user?.username}
                    />

                    <ProfileField
                        icon={Mail}
                        label="Email"
                        value={user?.email}
                    />

                    {role === "driver" && user?.licensePlate && (
                        <ProfileField
                            icon={Car}
                            label="License Plate"
                            value={user.licensePlate}
                        />
                    )}

                    {user?.paymentMethod && (
                        <ProfileField
                            icon={
                                user.paymentMethod === "Visa"
                                    ? CreditCard
                                    : Wallet
                            }
                            label="Payment Method"
                            value={
                                user.paymentMethod.charAt(0).toUpperCase() +
                                user.paymentMethod.slice(1)
                            }
                        />
                    )}
                </div>
            </div>
        </div>
    );
}
