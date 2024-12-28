import { Bell } from "lucide-react";
import { useWebSocket } from "./WebSocketProvider";
import "./NotificationBell.css";

export default function NotificationBell() {
    const { notifications } = useWebSocket();
    // clear notifications on click
    const handleClick = () => {
        // clear notifications
        notifications.splice(0, notifications.length);
    };

    return (
        <div className="notification-bell">
            <Bell className="bell-icon" />
            {notifications.length > 0 && (
                <span className="notification-badge">
                    {notifications.length}
                </span>
            )}
        </div>
    );
}
