import { useState } from 'react'

export default function TopUsersReport() {
  const [topUsers] = useState([
    { id: 1, name: 'John Doe', reservations: 45, totalSpent: 450 },
    { id: 2, name: 'Jane Smith', reservations: 38, totalSpent: 380 },
    { id: 3, name: 'Bob Johnson', reservations: 32, totalSpent: 320 },
  ])

  return (
    <div className="bg-white shadow rounded-lg p-6">
      <h2 className="text-xl font-semibold mb-4">Top Users by Reservations</h2>
      <div className="overflow-x-auto">
        <table className="min-w-full">
          <thead>
            <tr>
              <th className="px-6 py-3 border-b text-left">User</th>
              <th className="px-6 py-3 border-b text-left">Total Reservations</th>
              <th className="px-6 py-3 border-b text-left">Total Spent</th>
            </tr>
          </thead>
          <tbody>
            {topUsers.map(user => (
              <tr key={user.id}>
                <td className="px-6 py-4 border-b">{user.name}</td>
                <td className="px-6 py-4 border-b">{user.reservations}</td>
                <td className="px-6 py-4 border-b">${user.totalSpent}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  )
}