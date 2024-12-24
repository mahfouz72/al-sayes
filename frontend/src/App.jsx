import {
    BrowserRouter as Router,
    Routes,
    Route,
    Navigate,
} from "react-router-dom";
import Navbar from "./components/layout/Navbar";
import LoginForm from "./components/auth/LoginForm";
import SignupForm from "./components/auth/SignupForm";
import DriverDashboard from "./components/dashboard/DriverDashboard";
import ManagerDashboard from "./components/dashboard/ManagerDashboard";
import AdminDashboard from "./components/dashboard/AdminDashboard";
import UserProfile from "./components/profile/UserProfile";
import TopUsersReport from "./components/admin/TopUsersReport";
import TopLotsReport from "./components/admin/TopLotsReport";
import useAuthStore from "./store/authStore";
import "./index.css";

function PrivateRoute({ children, allowedRoles }) {
    const { isAuthenticated, role } = useAuthStore();

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
    const { role } = useAuthStore();

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
                <Route path="/" element={<Navigate to="/login" />} />
            </Routes>
        </Router>
    );
}

export default App;
