export default function UserStatusBadge({ status }) {
    const statusStyles = {
        ACTIVE: "bg-green-50 text-green-700 ring-green-600/20",
        DISABLED: "bg-red-50 text-red-700 ring-red-600/20",
    };

    return (
        <span
            className={`
        inline-flex items-center rounded-md px-2 py-1 text-sm font-medium
        ring-1 ring-inset ${
            statusStyles[status] || "bg-gray-50 text-gray-700 ring-gray-600/20"
        }
      `}
        >
            {status.charAt(0) + status.slice(1).toLowerCase()}
        </span>
    );
}
