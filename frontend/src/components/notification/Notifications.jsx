import {useWebSocket} from "./WebSocketProvider.jsx";

export default function Notifications() {

    const { notifications } = useWebSocket();

    return (
        <>
            <div>
                <h1>Notifications</h1>
                <ul>
                    {notifications.map((notification, index) => (
                        <li key={index}>{notification.message}</li>
                    ))}
                </ul>
            </div>
        </>
    );
}
