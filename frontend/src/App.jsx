import {BrowserRouter as Router, Navigate, Route, Routes,} from "react-router-dom";
import Navbar from "./components/layout/Navbar";
import LoginForm from "./components/auth/LoginForm";
import SignupForm from "./components/auth/SignupForm";
import DriverDashboard from "./components/dashboard/DriverDashboard";
import ManagerDashboard from "./components/dashboard/ManagerDashboard";
import AdminDashboard from "./components/dashboard/AdminDashboard";
import UserProfile from "./components/profile/UserProfile";
import TopUsersReport from "./components/admin/TopUsersReport";
import TopLotsReport from "./components/admin/TopLotsReport";
import ManageLots from "./components/parking/ManageLots";
import Reservations from "./components/reservations/Reservations";
import "./index.css";
import Notifications from "./components/notification/Notifications.jsx";
import {WebSocketProvider} from "./components/notification/WebSocketProvider.jsx";

function PrivateRoute({ children, allowedRoles }) {
    const role = localStorage.getItem("role");
    const isAuthenticated = localStorage.getItem("token");

    if (!isAuthenticated) {
        return <Navigate to="/login" />;
    }

    if (allowedRoles && !allowedRoles.includes(role)) {
        return <Navigate to="/dashboard" />;
    }

    return (
        <>
            <Navbar />
            {children}
        </>
    );
}

function DashboardRouter() {
    const role = localStorage.getItem("role");

    switch (role) {
        case "driver":
            return <DriverDashboard />;
        case "manager":
            return <ManagerDashboard />;
        case "admin":
            return <AdminDashboard />;
        default:
            return <Navigate to="/login" />;
    }
}

function App() {
    return (
        <WebSocketProvider>
        <Router>
            <Routes>
                <Route path="/login" element={<LoginForm />} />
                <Route path="/signup" element={<SignupForm />} />
                <Route
                    path="/dashboard"
                    element={
                        <PrivateRoute>
                            <DashboardRouter />
                        </PrivateRoute>
                    }
                />
                <Route
                    path="/reservations"
                    element={
                        <PrivateRoute>
                            <Reservations />
                        </PrivateRoute>
                    }
                />
                <Route
                    path="/manage-lots"
                    element={
                        <PrivateRoute allowedRoles={["manager"]}>
                            <ManageLots />
                        </PrivateRoute>
                    }
                />
                <Route
                    path="/profile"
                    element={
                        <PrivateRoute>
                            <UserProfile />
                        </PrivateRoute>
                    }
                />
                <Route
                    path="/reports"
                    element={
                        <PrivateRoute allowedRoles={["admin"]}>
                            <div className="max-w-7xl mx-auto p-6 space-y-6">
                                <TopUsersReport />
                                <TopLotsReport />
                            </div>
                        </PrivateRoute>
                    }
                />
                <Route
                    path="/notifications"
                    element={
                        <PrivateRoute>
                                <Notifications/>
                        </PrivateRoute>
                    }
                />
                <Route path="/" element={<Navigate to="/login" />} />
            </Routes>
        </Router>
        </WebSocketProvider>
    );
}

export default App;
