import { useState } from 'react'
import { MapPinIcon, ClockIcon } from '@heroicons/react/24/outline'

export default function ParkingLotCard({ lot, onReserve }) {
  const [showDetails, setShowDetails] = useState(false)

  return (
    <div className="bg-white rounded-xl shadow-lg overflow-hidden transition-all duration-300 hover:shadow-xl">
      <div className="p-6">
        <div className="flex justify-between items-start">
          <div>
            <h3 className="text-xl font-bold text-gray-900">{lot.name}</h3>
            <div className="flex items-center mt-2 text-gray-600">
              <MapPinIcon className="h-5 w-5 mr-2" />
              <p>{lot.location}</p>
            </div>
          </div>
          <div className="bg-blue-100 px-3 py-1 rounded-full">
            <span className="text-blue-800 font-semibold">
              ${lot.pricePerHour}/hr
            </span>
          </div>
        </div>

        <div className="mt-4">
          <div className="flex items-center justify-between text-sm">
            <span className="text-gray-600">Available spots</span>
            <span className="font-semibold">{lot.available}/{lot.total}</span>
          </div>
          <div className="w-full bg-gray-200 rounded-full h-2 mt-2">
            <div
              className="bg-blue-600 h-2 rounded-full"
              style={{ width: `${(lot.available / lot.total) * 100}%` }}
            ></div>
          </div>
        </div>

        <div className="mt-4 space-y-2">
          <div className="flex justify-between text-sm">
            <span className="text-gray-600">Regular spots:</span>
            <span>{lot.spotTypes.regular}</span>
          </div>
          <div className="flex justify-between text-sm">
            <span className="text-gray-600">Disabled spots:</span>
            <span>{lot.spotTypes.disabled}</span>
          </div>
          <div className="flex justify-between text-sm">
            <span className="text-gray-600">EV charging spots:</span>
            <span>{lot.spotTypes.ev}</span>
          </div>
        </div>

        <div className="mt-6 flex gap-4">
          <button
            onClick={() => onReserve(lot)}
            className="flex-1 bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors"
          >
            Reserve Spot
          </button>
          <button
            onClick={() => setShowDetails(!showDetails)}
            className="flex-1 border border-gray-300 px-4 py-2 rounded-lg hover:bg-gray-50 transition-colors"
          >
            {showDetails ? 'Hide Details' : 'View Details'}
          </button>
        </div>

        {showDetails && (
          <div className="mt-4 pt-4 border-t border-gray-200">
            <h4 className="font-semibold mb-2">Operating Hours</h4>
            <div className="flex items-center text-sm text-gray-600">
              <ClockIcon className="h-5 w-5 mr-2" />
              <span>24/7</span>
            </div>
            <div className="mt-2">
              <h4 className="font-semibold mb-2">Features</h4>
              <ul className="text-sm text-gray-600 list-disc list-inside">
                <li>Security cameras</li>
                <li>24/7 security personnel</li>
                <li>EV charging stations</li>
                <li>Covered parking</li>
              </ul>
            </div>
          </div>
        )}
      </div>
    </div>
  )
}