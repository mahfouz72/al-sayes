import { useState } from 'react'
import useAuthStore from '../../store/authStore'

export default function UserProfile() {
  const { user, role } = useAuthStore()
  const [isEditing, setIsEditing] = useState(false)
  const [formData, setFormData] = useState({
    name: user?.name || '',
    email: user?.email || '',
    phone: user?.phone || '',
    licensePlate: user?.licensePlate || ''
  })

  const handleSubmit = (e) => {
    e.preventDefault()
    // Handle profile update
    setIsEditing(false)
  }

  return (
    <div className="max-w-4xl mx-auto p-6">
      <h1 className="text-2xl font-bold mb-6">Profile Settings</h1>
      
      <div className="bg-white shadow rounded-lg p-6">
        <div className="flex justify-between items-center mb-6">
          <div className="flex items-center">
            <div className="h-20 w-20 rounded-full bg-gray-600 flex items-center justify-center text-white text-2xl">
              {user?.name?.[0] || 'U'}
            </div>
            <div className="ml-4">
              <h2 className="text-xl font-semibold">{user?.name}</h2>
              <p className="text-gray-600 capitalize">{role}</p>
            </div>
          </div>
          <button
            onClick={() => setIsEditing(!isEditing)}
            className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700"
          >
            {isEditing ? 'Cancel' : 'Edit Profile'}
          </button>
        </div>

        {isEditing ? (
          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700">Name</label>
              <input
                type="text"
                value={formData.name}
                onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">Email</label>
              <input
                type="email"
                value={formData.email}
                onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">Phone</label>
              <input
                type="tel"
                value={formData.phone}
                onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
              />
            </div>
            {role === 'driver' && (
              <div>
                <label className="block text-sm font-medium text-gray-700">License Plate</label>
                <input
                  type="text"
                  value={formData.licensePlate}
                  onChange={(e) => setFormData({ ...formData, licensePlate: e.target.value })}
                  className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                />
              </div>
            )}
            <div className="flex justify-end">
              <button
                type="submit"
                className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700"
              >
                Save Changes
              </button>
            </div>
          </form>
        ) : (
          <div className="space-y-4">
            <div>
              <h3 className="text-sm font-medium text-gray-500">Email</h3>
              <p className="mt-1">{user?.email}</p>
            </div>
            <div>
              <h3 className="text-sm font-medium text-gray-500">Phone</h3>
              <p className="mt-1">{user?.phone || 'Not provided'}</p>
            </div>
            {role === 'driver' && (
              <div>
                <h3 className="text-sm font-medium text-gray-500">License Plate</h3>
                <p className="mt-1">{user?.licensePlate || 'Not provided'}</p>
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  )
}