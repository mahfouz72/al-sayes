import { PencilIcon } from "lucide-react";

const roleMapping = {
    ROLE_DRIVER: "Driver",
    ROLE_ADMIN: "Admin",
    ROLE_MANAGER: "Manager",
};

export default function UserRoleDisplay({ role, onEdit }) {
    return (
        <div className="flex items-center gap-2 justify-center">
            <span className="text-sm text-gray-900">
                {roleMapping[role] || role}
            </span>
            <button
                onClick={onEdit}
                className="p-1 text-gray-400 hover:text-gray-600"
                title="Edit role"
            >
                <PencilIcon className="h-4 w-4" />
            </button>
        </div>
    );
}
