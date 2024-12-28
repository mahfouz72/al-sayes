import { useState, useEffect } from "react";
import {
    LineChart,
    Line,
    XAxis,
    YAxis,
    CartesianGrid,
    Tooltip,
    Legend,
} from "recharts";
import axios from "axios";
import { AlertCircle } from "lucide-react";
import UserActionButtons from "./UserActionButtons";
import UserStatusBadge from "./UserStatusBadge";
import Pagination from "./Pagination";
import UsersTable from "./UsersTable";

export default function AdminDashboard() {
    const [users, setUsers] = useState([]);
    const [systemStats, setSystemStats] = useState({});
    const [systemRevenueGraph, setSystemReveneuGraph] = useState([]);
    const [systemSpotsGraph, setSystemSpotsGraph] = useState([]);
    const [loading, setLoading] = useState(true);
    const [currentPage, setCurrentPage] = useState(1);
    const [pageSize] = useState(5);
    const [limit, setLimit] = useState(10);
    const [editingUser, setEditingUser] = useState(null);
    const [role, setRole] = useState("");
    const [limitError, setLimitError] = useState("");
    const [userState, setUserStatus] = useState("");

    const token = localStorage.getItem("token");
    let headers = {
        Authorization: `Bearer ${token}`,
    };

    const fetchData = async (url, setter, method = "GET", data = null) => {
        try {
            let response;
            if (method === "POST") {
                response = await axios.post(url, data, { headers });
            } else {
                response = await axios.get(url, { headers });
            }
            setter(response.data);
            console.log(response.data);
        } catch (error) {
            console.error("Error fetching data:", error);
        }
    };

    const fetchUsers = async () => {
        fetchData(
            `http://localhost:8080/api/statistics/users?page=${currentPage}&size=${pageSize}`,
            setUsers,
            "POST",
            { currentPage, pageSize }
        );
    };

    useEffect(() => {
        fetchData(
            "http://localhost:8080/api/statistics/totals",
            setSystemStats,
            "GET"
        );

        fetchData(
            `http://localhost:8080/api/statistics/daily-revenue?limit=${limit}`,
            setSystemReveneuGraph,
            "POST",
            { limit }
        );

        fetchData(
            `http://localhost:8080/api/statistics/daily-reserved-spots?limit=${limit}`,
            setSystemSpotsGraph,
            "POST",
            { limit }
        );

        setLoading(false);
    }, [limit]);

    useEffect(() => {
        fetchUsers();
    }, [currentPage]);

    if (loading) {
        return <div>Loading...</div>;
    }

    const totalPages = Math.ceil(systemStats.totalUsers / pageSize);
    const handlePageChange = (newPage) => {
        if (newPage < 1 || newPage > totalPages) return;
        setCurrentPage(newPage);
    };

    const handleLimit = (e) => {
        const value = e.target.value;
        const numValue = parseInt(value, 10);

        // Clear error when input is empty
        if (value === "") {
            setLimit("");
            setLimitError("");
            return;
        }

        // Validate input
        if (isNaN(numValue)) {
            setLimitError("Please enter a valid number");
            return;
        }

        if (numValue < 1) {
            setLimitError("Limit must be at least 1");
            return;
        }

        if (numValue > 100) {
            setLimitError("Limit cannot exceed 100");
            return;
        }

        setLimit(numValue);
        setLimitError("");
    };

    const roleMapping = {
        ROLE_DRIVER: "Driver",
        ROLE_ADMIN: "Admin",
        ROLE_MANAGER: "Manager",
    };

    const handleActivateUser = (username) => {
        fetchData(
            `http://localhost:8080/api/statistics/unblock-user?username=${username}`,
            setUserStatus,
            "POST",
            { username }
        ).then(() => {
            fetchUsers();
        });
    };

    const handleDisableUser = (username) => {
        fetchData(
            `http://localhost:8080/api/statistics/block-user?username=${username}`,
            setUserStatus,
            "POST",
            { username }
        ).then(() => {
            fetchUsers();
        });
    };

    const handleRoleChange = async (username, newRole) => {
        try {
            await fetchData(
                `http://localhost:8080/api/statistics/change-role?username=${username}&role=${newRole}`,
                setUserStatus,
                "POST",
                { username, role: newRole }
            ).then(() => {
                fetchUsers();
            });
        } catch (error) {
            console.error("Error changing user role:", error);
        }
    };
    return (
        <div className="p-6">
            <h1 className="text-2xl font-bold mb-6">System Administration</h1>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-8">
                <div className="bg-blue-100 p-4 rounded-lg">
                    <h3 className="font-bold">Total Users</h3>
                    <p className="text-2xl">{systemStats.totalUsers}</p>
                </div>
                <div className="bg-green-100 p-4 rounded-lg">
                    <h3 className="font-bold">Total Managers</h3>
                    <p className="text-2xl">{systemStats.totalManagers}</p>
                </div>
                <div className="bg-gray-100 p-4 rounded-lg">
                    <h3 className="font-bold">Total Drivers</h3>
                    <p className="text-2xl">{systemStats.totalManagers}</p>
                </div>
                <div className="bg-cyan-100 p-4 rounded-lg">
                    <h3 className="font-bold">Parking Lots</h3>
                    <p className="text-2xl">{systemStats.totalParkingLots}</p>
                </div>
                <div className="bg-yellow-100 p-4 rounded-lg">
                    <h3 className="font-bold">Monthly Revenue</h3>
                    <p className="text-2xl">${systemStats.monthlyRevenue}</p>
                </div>
                <div className="bg-purple-100 p-4 rounded-lg">
                    <h3 className="font-bold">Total Revenue</h3>
                    <p className="text-2xl">${systemStats.totalRevenue}</p>
                </div>
            </div>
            <div className="mb-6">
                <label className="block text-sm font-medium text-gray-700 mb-2">
                    Number of days to display in graphs
                </label>
                <div className="relative">
                    <input
                        type="number"
                        min="1"
                        max="100"
                        placeholder="Enter days (1-100)"
                        value={limit}
                        onChange={handleLimit}
                        className={`
              w-48 px-4 py-2 border rounded-md shadow-sm
              focus:ring-2 focus:ring-blue-500 focus:border-blue-500
              ${limitError ? "border-red-500" : "border-gray-300"}
            `}
                    />
                    {limitError && (
                        <div className="absolute mt-1 flex items-center text-sm text-red-600">
                            <AlertCircle className="w-4 h-4 mr-1" />
                            {limitError}
                        </div>
                    )}
                </div>
            </div>

            <div className="mb-8 flex flex-wrap">
                <h2 className="text-xl font-semibold mb-4">Revenue Trend</h2>
                <div className="w-full overflow-x-auto">
                    <LineChart
                        width={1200}
                        height={360}
                        data={systemRevenueGraph}
                    >
                        <CartesianGrid strokeDasharray="3 3" />
                        <XAxis
                            dataKey="date"
                            angle={50}
                            textAnchor="start"
                            interval={0}
                            height={60}
                        />
                        <YAxis />
                        <Tooltip />
                        <Legend />
                        <Line
                            type="monotone"
                            dataKey="revenue"
                            stroke="#8884d8"
                        />
                    </LineChart>
                </div>
                <h2 className="text-xl font-semibold mb-4">Slots Trend</h2>
                <div className="w-full overflow-x-auto">
                    <LineChart
                        width={1200}
                        height={360}
                        data={systemSpotsGraph}
                    >
                        <CartesianGrid strokeDasharray="3 3" />
                        <XAxis
                            dataKey="date"
                            angle={50}
                            textAnchor="start"
                            height={60}
                        />
                        <YAxis />
                        <Tooltip />
                        <Legend />
                        <Line
                            type="monotone"
                            dataKey="reserved_spots"
                            stroke="#8884d8"
                        />
                    </LineChart>
                </div>
            </div>

            <div className="mb-8">
                <h2 className="text-xl font-semibold mb-4">User Management</h2>
                <UsersTable
                    users={users}
                    onActivate={handleActivateUser}
                    onDisable={handleDisableUser}
                    onRoleChange={handleRoleChange}
                />
                {systemStats.totalUsers > 0 && (
                    <Pagination
                        currentPage={currentPage}
                        totalPages={totalPages}
                        onPageChange={handlePageChange}
                        totalItems={systemStats.totalUsers}
                        pageSize={pageSize}
                    />
                )}
            </div>
        </div>
    );
}
