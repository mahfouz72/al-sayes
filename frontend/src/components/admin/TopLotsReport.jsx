import { useState } from 'react'
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend } from 'recharts'

export default function TopLotsReport() {
  const [topLots] = useState([
    { name: 'Downtown Parking', revenue: 25000, occupancyRate: 85 },
    { name: 'Mall Parking', revenue: 18000, occupancyRate: 75 },
    { name: 'Airport Parking', revenue: 15000, occupancyRate: 90 },
  ])

  return (
    <div className="bg-white shadow rounded-lg p-6">
      <h2 className="text-xl font-semibold mb-4">Top Performing Parking Lots</h2>
      
      <div className="mb-6">
        <BarChart width={600} height={300} data={topLots}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="name" />
          <YAxis yAxisId="left" orientation="left" stroke="#8884d8" />
          <YAxis yAxisId="right" orientation="right" stroke="#82ca9d" />
          <Tooltip />
          <Legend />
          <Bar yAxisId="left" dataKey="revenue" fill="#8884d8" name="Revenue ($)" />
          <Bar yAxisId="right" dataKey="occupancyRate" fill="#82ca9d" name="Occupancy Rate (%)" />
        </BarChart>
      </div>

      <div className="overflow-x-auto">
        <table className="min-w-full">
          <thead>
            <tr>
              <th className="px-6 py-3 border-b text-left">Parking Lot</th>
              <th className="px-6 py-3 border-b text-left">Revenue</th>
              <th className="px-6 py-3 border-b text-left">Occupancy Rate</th>
            </tr>
          </thead>
          <tbody>
            {topLots.map((lot, index) => (
              <tr key={index}>
                <td className="px-6 py-4 border-b">{lot.name}</td>
                <td className="px-6 py-4 border-b">${lot.revenue}</td>
                <td className="px-6 py-4 border-b">{lot.occupancyRate}%</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  )
}