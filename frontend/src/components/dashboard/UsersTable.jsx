import UserTableRow from "./UserTableRow";

export default function UsersTable({
    users,
    onActivate,
    onDisable,
    onRoleChange,
}) {
    return (
        <div className="overflow-x-auto">
            <table className="min-w-full bg-white">
                <thead>
                    <tr>
                        <th className="px-6 py-3 border-b text-left text-sm font-semibold text-gray-900 text-center">
                            Name
                        </th>
                        <th className="px-6 py-3 border-b text-left text-sm font-semibold text-gray-900 text-center">
                            Role
                        </th>
                        <th className="px-6 py-3 border-b text-left text-sm font-semibold text-gray-900 text-center">
                            Status
                        </th>
                        <th className="px-6 py-3 border-b text-left text-sm font-semibold text-gray-900 text-center">
                            Actions
                        </th>
                    </tr>
                </thead>
                <tbody className="text-center">
                    {users.map((user) => (
                        <UserTableRow
                            key={user.username}
                            user={user}
                            onActivate={onActivate}
                            onDisable={onDisable}
                            onRoleChange={onRoleChange}
                        />
                    ))}
                </tbody>
            </table>
        </div>
    );
}
