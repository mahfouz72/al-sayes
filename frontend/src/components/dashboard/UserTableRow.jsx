import { useState } from "react";
import UserStatusBadge from "./UserStatusBadge";
import UserActionButtons from "./UserActionButtons";
import UserRoleSelect from "./UserRoleSelect";
import UserRoleDisplay from "./UserRoleDisplay";

export default function UserTableRow({
    user,
    onActivate,
    onDisable,
    onRoleChange,
}) {
    const [isEditing, setIsEditing] = useState(false);

    const handleSaveRole = (newRole) => {
        onRoleChange(user.username, newRole);
        setIsEditing(false);
    };

    return (
        <tr key={user.username}>
            <td className="px-6 py-4 border-b font-semibold">
                {user.username}
            </td>
            <td className="px-6 py-4 border-b text-center">
                {isEditing ? (
                    <UserRoleSelect
                        currentRole={user.role}
                        onSave={handleSaveRole}
                        onCancel={() => setIsEditing(false)}
                    />
                ) : (
                    <UserRoleDisplay
                        role={user.role}
                        onEdit={() => setIsEditing(true)}
                    />
                )}
            </td>
            <td className="px-6 py-4 border-b">
                <UserStatusBadge status={user.status} />
            </td>
            <td className="px-6 py-4 border-b">
                <UserActionButtons
                    status={user.status}
                    onActivate={() => onActivate(user.username)}
                    onDisable={() => onDisable(user.username)}
                />
            </td>
        </tr>
    );
}
