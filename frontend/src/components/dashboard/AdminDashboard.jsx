import { useState } from 'react'
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend } from 'recharts'

export default function AdminDashboard() {
  const [users, setUsers] = useState([
    { id: 1, name: 'John Doe', role: 'driver', status: 'active' },
    { id: 2, name: 'Jane Smith', role: 'manager', status: 'active' },
  ])

  const [systemStats] = useState({
    totalUsers: 150,
    totalParkingLots: 10,
    totalRevenue: 25000,
    recentActivity: [
      { date: '2024-03-01', revenue: 1200 },
      { date: '2024-03-02', revenue: 1500 },
      { date: '2024-03-03', revenue: 1300 },
    ]
  })

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-6">System Administration</h1>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-8">
        <div className="bg-blue-100 p-4 rounded-lg">
          <h3 className="font-bold">Total Users</h3>
          <p className="text-2xl">{systemStats.totalUsers}</p>
        </div>
        <div className="bg-green-100 p-4 rounded-lg">
          <h3 className="font-bold">Parking Lots</h3>
          <p className="text-2xl">{systemStats.totalParkingLots}</p>
        </div>
        <div className="bg-purple-100 p-4 rounded-lg">
          <h3 className="font-bold">Total Revenue</h3>
          <p className="text-2xl">${systemStats.totalRevenue}</p>
        </div>
      </div>

      <div className="mb-8">
        <h2 className="text-xl font-semibold mb-4">Revenue Trend</h2>
        <div className="w-full overflow-x-auto">
          <LineChart width={600} height={300} data={systemStats.recentActivity}>
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="date" />
            <YAxis />
            <Tooltip />
            <Legend />
            <Line type="monotone" dataKey="revenue" stroke="#8884d8" />
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
                <tr key={user.id}>
                  <td className="px-6 py-4 border-b">{user.name}</td>
                  <td className="px-6 py-4 border-b">{user.role}</td>
                  <td className="px-6 py-4 border-b">{user.status}</td>
                  <td className="px-6 py-4 border-b">
                    <button className="text-blue-500 hover:text-blue-700 mr-2">Edit</button>
                    <button className="text-red-500 hover:text-red-700">Disable</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  )
}