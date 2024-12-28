import { createContext, useContext, useEffect, useState } from "react";
import { Client } from "@stomp/stompjs";
import { getUserToken } from "../../apis/config.js";

const WebSocketContext = createContext();

export const useWebSocket = () => {
    return useContext(WebSocketContext);
};

export const WebSocketProvider = ({ children }) => {
    const [notifications, setNotifications] = useState([]);
    const [client, setClient] = useState(null);
    const [token, setToken] = useState(null);

    if (token !== getUserToken()) {
        setToken(getUserToken());
    }

    useEffect(() => {
        if (!token) {
            return;
        }

        const client = new Client({
            brokerURL: `ws://localhost:8080/ws?token=${getUserToken()}`,
            onConnect: () => {
                console.log("WebSocket connected");
                client.subscribe("/topic/notifications", (message) => {
                    console.log("Received message:", message.body);
                    const notification = JSON.parse(message.body);
                    setNotifications((prevNotifications) => [
                        notification,
                        ...prevNotifications,
                    ]);
                });
            },
            onStompError: (frame) => {
                console.error("WebSocket error:", frame);
            },
            onDisconnect: () => {
                console.log("WebSocket disconnected");
            },
        });

        client.activate();
        setClient(client);

        return () => {
            if (client.active) {
                client.deactivate();
                console.log("WebSocket deactivated");
            }
        };
    }, [token]);

    return (
        <WebSocketContext.Provider value={{ notifications }}>
            {children}
        </WebSocketContext.Provider>
    );
};
