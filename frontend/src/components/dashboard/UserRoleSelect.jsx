import { PencilIcon, CheckIcon, XIcon } from "lucide-react";
import { useState } from "react";

export default function UserRoleSelect({ currentRole, onSave, onCancel }) {
    const [selectedRole, setSelectedRole] = useState(currentRole);

    const roleOptions = [
        { value: "ROLE_ADMIN", label: "Admin" },
        { value: "ROLE_MANAGER", label: "Manager" },
        { value: "ROLE_DRIVER", label: "Driver" },
    ];

    return (
        <div className="flex items-center gap-2 justify-center">
            <select
                value={selectedRole}
                onChange={(e) => setSelectedRole(e.target.value)}
                className="block w-32 rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 text-sm"
            >
                {roleOptions.map((role) => (
                    <option key={role.value} value={role.value}>
                        {role.label}
                    </option>
                ))}
            </select>

            <button
                onClick={() => onSave(selectedRole)}
                className="p-1 text-green-600 hover:text-green-800"
                title="Save"
            >
                <CheckIcon className="h-4 w-4" />
            </button>

            <button
                onClick={onCancel}
                className="p-1 text-red-600 hover:text-red-800"
                title="Cancel"
            >
                <XIcon className="h-4 w-4" />
            </button>
        </div>
    );
}
