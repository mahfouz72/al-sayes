import { useState, useEffect } from 'react'
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend } from 'recharts'
import axios from 'axios';

export default function AdminDashboard() {
  const [users, setUsers] = useState([]);
  const [systemStats, setSystemStats] = useState({});
  const [systemRevenueGraph, setSystemReveneuGraph] = useState([]);
  const [systemSpotsGraph, setSystemSpotsGraph] = useState([]);
  const [loading, setLoading] = useState(true);
  const [currentPage, setCurrentPage] = useState(1);
  const [pageSize] = useState(10);
  const [limit, setLimit] = useState(10);
  const [editingUser, setEditingUser] = useState(null);
  const [role, setRole] = useState('');



  const fetchData = async (url, setter, method = 'GET', data = null) => {
    try {
      let response;
      if (method === 'POST') {
        response = await axios.post(url, data);
      } else {
        response = await axios.get(url);
      }
      setter(response.data);
      console.log(response.data);
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };

  useEffect(() => {
    fetchData("http://localhost:8080/api/statistics/totals", setSystemStats, 'GET');

    fetchData(`http://localhost:8080/api/statistics/daily-revenue?limit=${limit}`, setSystemReveneuGraph, 'POST', { limit });

    fetchData(`http://localhost:8080/api/statistics/daily-reserved-spots?limit=${limit}`, setSystemSpotsGraph, 'POST', { limit });
    
    setLoading(false);
  }, [limit]);

  useEffect(() => {
    fetchData(`http://localhost:8080/api/statistics/users?page=${currentPage}&size=${pageSize}`, setUsers, 'POST', { currentPage, pageSize});

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
    if (value === '' || !isNaN(value)) {
      setLimit(value);
    }
  };

  const roleMapping = {
    ROLE_USER: "User",
    ROLE_ADMIN: "Admin",
    ROLE_MANAGER: "Manager",
  };

  const handleRoleChange = (event, username) => {
    const newRole = event.target.value;
    setRole(newRole);

    const updatedUsers = users.map(user => 
      user.username === username ? { ...user, role: newRole } : user
    );
    setEditingUser(updatedUsers);
  };

  const handleEditClick = (user) => {
    setEditingUser(user.username);
    setRole(user.role); 
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

      <div className="mb-8 flex flex-wrap">
        <h2 className="text-xl font-semibold mb-4">Revenue Trend</h2>
        <input
          placeholder="Limit"
          value={limit}
          onChange={handleLimit}
          style={{
            border: '1px solid black',
            padding: '2px 2px 2px 2px',
            margin: '10px 10px 10px 10px',
            fontSize: '14px',
            borderRadius: '5px'
           }}/>
        <div className="w-full overflow-x-auto">
          <LineChart width={1400} height={300} data={systemRevenueGraph}>
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="date" />
            <YAxis />
            <Tooltip />
            <Legend />
            <Line type="monotone" dataKey="revenue" stroke="#8884d8" />
          </LineChart>
        </div>
        <h2 className="text-xl font-semibold mb-4">Slots Trend</h2>
        <div className="w-full overflow-x-auto">
          <LineChart width={1400} height={300} data={systemSpotsGraph}>
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="date" />
            <YAxis />
            <Tooltip />
            <Legend />
            <Line type="monotone" dataKey="reserved_spots" stroke="#8884d8" />
          </LineChart>
        </div>
      </div>

      

      <div className="mb-8">
        <h2 className="text-xl font-semibold mb-4">User Management</h2>
        <div className="overflow-x-auto">
          <table className="min-w-full bg-white">
            <thead>
              <tr>
                <th className="px-6 py-3 border-b">Name</th>
                <th className="px-6 py-3 border-b">Role</th>
                <th className="px-6 py-3 border-b">Status</th>
                <th className="px-6 py-3 border-b">Actions</th>
              </tr>
            </thead>
            <tbody>
            {users.map(user => (
              <tr key={user.username}>
                <td className="px-6 py-4 border-b">{user.username}</td>
                <td className="px-6 py-4 border-b">
                  {editingUser === user.username ? (
                    <select
                      value={role}
                      onChange={(e) => handleRoleChange(e, user.username)}
                      className="border rounded-md"
                    >
                      <option value="ROLE_ADMIN">Admin</option>
                      <option value="ROLE_USER">User</option>
                      <option value="ROLE_MANAGER">Manager</option>
                    </select>
                  ) : (
                    roleMapping[user.role] || user.role
                  )}
                </td>
                <td className="px-6 py-4 border-b">Active</td>
                <td className="px-6 py-4 border-b">
                  <button className="text-blue-500 hover:text-blue-700 mr-2">Edit</button>
                  <button className="text-red-500 hover:text-red-700">Disable</button>
                </td>
              </tr>
            ))}
          </tbody>
          </table>
        </div>
        <div className="flex justify-between items-center mt-4">
          <button 
            className="px-4 py-2 bg-gray-200 rounded-md hover:bg-gray-300" 
            onClick={() => handlePageChange(currentPage - 1)} 
            disabled={currentPage === 1}
          >
            Previous
          </button>
          <span>Page {currentPage} of {totalPages}</span>
          <button 
            className="px-4 py-2 bg-gray-200 rounded-md hover:bg-gray-300" 
            onClick={() => handlePageChange(currentPage + 1)} 
            disabled={currentPage === totalPages}
          >
            Next
          </button>
        </div>
      </div>
    </div>
  );
}