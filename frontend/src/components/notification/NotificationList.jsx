import { useWebSocket } from "./WebSocketProvider";
import "./NotificationList.css";

export default function NotificationList() {
    const { notifications } = useWebSocket();
    const localNotifications = [...notifications];
    // notifications.splice(0, notifications.length);
    if (localNotifications.length === 0) {
        return <div className="no-notifications">No notifications</div>;
    }

    return (
        <div className="notification-list">
            {localNotifications.map((notification, index) => (
                <div key={index} className="notification-item">
                    <div className="notification-content">
                        <p className="notification-message">
                            {notification.message}
                        </p>
                        {/* <span className="notification-time">
                            {new Date(notification.timestamp).toLocaleString()}
                        </span> */}
                    </div>
                </div>
            ))}
        </div>
    );
}
