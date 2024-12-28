import NotificationList from "../notification/NotificationList";
import "./NotificationsPage.css";

export default function NotificationsPage() {
    return (
        <div className="notifications-page">
            <h1>Notifications</h1>
            <NotificationList />
        </div>
    );
}
