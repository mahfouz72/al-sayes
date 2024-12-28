import { Power, PowerOff } from "lucide-react";

export default function UserActionButtons({ onActivate, onDisable, status }) {
    return (
        <div className="flex gap-2 justify-center">
            <button
                onClick={onActivate}
                disabled={status === "ACTIVE"}
                className={`
          inline-flex items-center px-3 py-1.5 rounded-md text-sm font-medium
          ${
              status === "ACTIVE"
                  ? "bg-gray-100 text-gray-400 cursor-not-allowed"
                  : "bg-green-50 text-green-600 hover:bg-green-100"
          }
          transition-colors duration-200
        `}
            >
                <Power className="w-4 h-4 mr-1" />
                Activate
            </button>
            <button
                onClick={onDisable}
                disabled={status === "BLOCKED"}
                className={`
          inline-flex items-center px-3 py-1.5 rounded-md text-sm font-medium
          ${
              status === "BLOCKED"
                  ? "bg-gray-100 text-gray-400 cursor-not-allowed"
                  : "bg-red-50 text-red-600 hover:bg-red-100"
          }
          transition-colors duration-200
        `}
            >
                <PowerOff className="w-4 h-4 mr-1" />
                Disable
            </button>
        </div>
    );
}
