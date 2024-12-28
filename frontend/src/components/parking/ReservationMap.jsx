import { useState, useEffect } from 'react'

export default function ReservationMap({ location }) {
  const [directions, setDirections] = useState(null)
  const [isLoading, setIsLoading] = useState(true)

  useEffect(() => {
    // Simulate getting directions
    setTimeout(() => {
      setDirections({
        distance: '2.5 km',
        duration: '8 mins',
        steps: [
          'Head north on Main St',
          'Turn right onto Oak Ave',
          'Turn left onto Park Rd',
          'Destination will be on your right'
        ]
      })
      setIsLoading(false)
    }, 1000)
  }, [location])

  if (isLoading) {
    return <div className="text-center py-4">Loading directions...</div>
  }

  return (
    <div className="bg-white rounded-lg shadow p-4">
      <h3 className="font-semibold mb-2">Directions to Parking Lot</h3>
      <div className="mb-4">
        <p className="text-sm text-gray-600">Distance: {directions.distance}</p>
        <p className="text-sm text-gray-600">Estimated time: {directions.duration}</p>
      </div>
      <div className="space-y-2">
        {directions.steps.map((step, index) => (
          <div key={index} className="flex items-start">
            <span className="bg-blue-100 text-blue-800 px-2 py-1 rounded mr-2">
              {index + 1}
            </span>
            <p className="text-sm">{step}</p>
          </div>
        ))}
      </div>
    </div>
  )
}