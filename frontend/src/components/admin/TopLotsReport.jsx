import { useState, useEffect } from 'react'
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend } from 'recharts'
import axios from 'axios';
import { AlertCircle } from 'lucide-react';


export default function TopLotsReport() {
  const [topLots, setTopLots] = useState([]);
  const [limit, setLimit] = useState(10);
  const [limitError, setLimitError] = useState('');

  const token = localStorage.getItem('token');
  let headers = {
    'Authorization': `Bearer ${token}`
  };


  const fetchData = async (url, setter, method = 'GET', data = null) => {
    try {
      let response;
      if (method === 'POST') {
        response = await axios.post(url, data, headers);
      } else {
        response = await axios.get(url, headers);
      }
      setter(response.data);
      console.log(response.data);
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };

  useEffect(() => {
    fetchData(`http://localhost:8080/api/statistics/top-parking-lots?limit=${limit}`, setTopLots, 'POST', { limit });
    
  }, [limit]);


  const handleLimit = (e) => {
    const value = e.target.value;
    const numValue = parseInt(value, 10);
    
    // Clear error when input is empty
    if (value === '') {
      setLimit('');
      setLimitError('');
      return;
    }
    
    // Validate input
    if (isNaN(numValue)) {
      setLimitError('Please enter a valid number');
      return;
    }
    
    if (numValue < 1) {
      setLimitError('Limit must be at least 1');
      return;
    }
    
    if (numValue > 100) {
      setLimitError('Limit cannot exceed 100');
      return;
    }
    
    setLimit(numValue);
    setLimitError('');
  };

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
          <Bar yAxisId="left" dataKey="total_revenue" fill="#8884d8" name="Revenue ($)" />
          <Bar yAxisId="right" dataKey="occupancy_rate" fill="#82ca9d" name="Occupancy Rate (%)" />
        </BarChart>
      </div>

      <div className="overflow-x-auto">
      <div className="mb-6">
        <label className="block text-sm font-medium text-gray-700 mb-2">
          Number of parkinglots to display
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
              ${limitError ? 'border-red-500' : 'border-gray-300'}
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
                <td className="px-6 py-4 border-b">${lot.total_revenue}</td>
                <td className="px-6 py-4 border-b">{lot.occupancy_rate}%</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  )
}