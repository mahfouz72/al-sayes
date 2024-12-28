import { useState, useEffect } from 'react'
import axios from 'axios';
import { AlertCircle } from 'lucide-react';


export default function TopUsersReport() {
  const [topUsers, setTopUsers] = useState([]);
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

  useEffect(() => {
    fetchData(`http://localhost:8080/api/statistics/top-users?limit=${limit}`, setTopUsers, 'POST', { limit });
    
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

  const handleDownloadUsers = async () => {
    const reportName = 'topUsers';
      const response = await fetch(`http://localhost:8080/api/statistics/users-report/${reportName}`, {
          method: 'GET',
          headers: {
              'Content-Type': 'application/pdf',
              'Authorization': `Bearer ${token}`

          }
      });
  
      if (response.ok) {
          const blob = await response.blob();
          const url = window.URL.createObjectURL(blob);
          const a = document.createElement('a');
          a.href = url;
          a.download = `${reportName}.pdf`;
          document.body.appendChild(a);
          a.click();
          a.remove();
      } else {
          console.error('Failed to fetch PDF');
      }
  };




  return (
    <div className="bg-white shadow rounded-lg p-6">
      <h2 className="text-xl font-semibold mb-4">Top Users by Reservations</h2>
      <div className="text-right">
          <button
            onClick={handleDownloadUsers}
            className="py-2 px-4 bg-blue-500 text-white font-medium text-sm rounded-lg hover:bg-blue-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-50 transition-all"
            >
            Download
          </button>
        </div>
      <div className="mb-6">
        <label className="block text-sm font-medium text-gray-700 mb-2">
          Number of users to display
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
              <tr key={user.username}>
                <td className="px-6 py-4 border-b">{user.username}</td>
                <td className="px-6 py-4 border-b">{user.total_reservations}</td>
                <td className="px-6 py-4 border-b">${user.total_spent}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  )
}