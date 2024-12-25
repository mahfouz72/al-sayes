import { useState } from 'react';
import { PlusIcon, PencilIcon, TrashIcon } from '@heroicons/react/24/outline';
import ParkingSpotGrid from './ParkingSpotGrid';
import AddEditLotModal from './modals/AddEditLotModal';
import SpotManagementModal from './modals/SpotManagementModal';

const mockParkingLots = [
  {
    id: 1,
    name: 'Downtown Parking',
    location: '123 Main St',
    totalSpots: 100,
    pricePerHour: 5,
    spots: Array(100).fill(null).map((_, i) => ({
      id: i + 1,
      number: `A${i + 1}`,
      type: i < 80 ? 'REGULAR' : i < 90 ? 'DISABLED' : 'EV',
      status: Math.random() > 0.5 ? 'AVAILABLE' : 'OCCUPIED'
    }))
  },
  {
    id: 2,
    name: 'Mall Parking',
    location: '456 Shopping Ave',
    totalSpots: 150,
    pricePerHour: 3,
    spots: Array(150).fill(null).map((_, i) => ({
      id: i + 1,
      number: `B${i + 1}`,
      type: i < 120 ? 'REGULAR' : i < 140 ? 'DISABLED' : 'EV',
      status: Math.random() > 0.5 ? 'AVAILABLE' : 'OCCUPIED'
    }))
  }
];

export default function ManageLots() {
  const [parkingLots, setParkingLots] = useState(mockParkingLots);
  const [selectedLot, setSelectedLot] = useState(null);
  const [showSpotGrid, setShowSpotGrid] = useState(false);
  const [showAddEditModal, setShowAddEditModal] = useState(false);
  const [showSpotModal, setShowSpotModal] = useState(false);
  const [selectedSpot, setSelectedSpot] = useState(null);
  const [editingLot, setEditingLot] = useState(null);

  const handleAddLot = () => {
    setEditingLot(null);
    setShowAddEditModal(true);
  };

  const handleEditLot = (lot) => {
    setEditingLot(lot);
    setShowAddEditModal(true);
  };

  const handleDeleteLot = (lotId) => {
    if (window.confirm('Are you sure you want to delete this parking lot?')) {
      setParkingLots(parkingLots.filter(lot => lot.id !== lotId));
    }
  };

  const handleSpotUpdate = (spot) => {
    setSelectedSpot(spot);
    setShowSpotModal(true);
  };

  const handleAddSpot = () => {
    setSelectedSpot(null);
    setShowSpotModal(true);
  };

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="sm:flex sm:items-center sm:justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Manage Parking Lots</h1>
          <p className="mt-2 text-sm text-gray-700">
            Add, edit, and manage your parking lots
          </p>
        </div>
        <button
          type="button"
          onClick={handleAddLot}
          className="mt-4 sm:mt-0 inline-flex items-center justify-center rounded-md bg-blue-600 px-3 py-2 text-sm font-semibold text-white shadow-sm hover:bg-blue-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-blue-600"
        >
          <PlusIcon className="-ml-0.5 mr-1.5 h-5 w-5" aria-hidden="true" />
          Add Parking Lot
        </button>
      </div>

      <div className="mt-8 flow-root">
        <div className="-mx-4 -my-2 overflow-x-auto sm:-mx-6 lg:-mx-8">
          <div className="inline-block min-w-full py-2 align-middle sm:px-6 lg:px-8">
            <div className="overflow-hidden shadow ring-1 ring-black ring-opacity-5 sm:rounded-lg">
              <table className="min-w-full divide-y divide-gray-300">
                <thead className="bg-gray-50">
                  <tr>
                    <th scope="col" className="py-3.5 pl-4 pr-3 text-left text-sm font-semibold text-gray-900 sm:pl-6">
                      Name
                    </th>
                    <th scope="col" className="px-3 py-3.5 text-left text-sm font-semibold text-gray-900">
                      Location
                    </th>
                    <th scope="col" className="px-3 py-3.5 text-left text-sm font-semibold text-gray-900">
                      Total Spots
                    </th>
                    <th scope="col" className="px-3 py-3.5 text-left text-sm font-semibold text-gray-900">
                      Price/Hour
                    </th>
                    <th scope="col" className="px-3 py-3.5 text-left text-sm font-semibold text-gray-900">
                      Status
                    </th>
                    <th scope="col" className="relative py-3.5 pl-3 pr-4 sm:pr-6">
                      <span className="sr-only">Actions</span>
                    </th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-200 bg-white">
                  {parkingLots.map((lot) => (
                    <tr key={lot.id}>
                      <td className="whitespace-nowrap py-4 pl-4 pr-3 text-sm font-medium text-gray-900 sm:pl-6">
                        {lot.name}
                      </td>
                      <td className="whitespace-nowrap px-3 py-4 text-sm text-gray-500">{lot.location}</td>
                      <td className="whitespace-nowrap px-3 py-4 text-sm text-gray-500">{lot.totalSpots}</td>
                      <td className="whitespace-nowrap px-3 py-4 text-sm text-gray-500">${lot.pricePerHour}</td>
                      <td className="whitespace-nowrap px-3 py-4 text-sm text-gray-500">
                        <span className="inline-flex items-center rounded-full bg-green-100 px-2 py-1 text-xs font-medium text-green-700">
                          Active
                        </span>
                      </td>
                      <td className="relative whitespace-nowrap py-4 pl-3 pr-4 text-right text-sm font-medium sm:pr-6">
                        <button
                          onClick={() => {
                            setSelectedLot(lot);
                            setShowSpotGrid(!showSpotGrid);
                          }}
                          className="text-blue-600 hover:text-blue-900 mr-4"
                        >
                          View Spots
                        </button>
                        <button
                          onClick={() => handleEditLot(lot)}
                          className="text-blue-600 hover:text-blue-900 mr-4"
                        >
                          <PencilIcon className="h-5 w-5" />
                        </button>
                        <button
                          onClick={() => handleDeleteLot(lot.id)}
                          className="text-red-600 hover:text-red-900"
                        >
                          <TrashIcon className="h-5 w-5" />
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>

      {selectedLot && showSpotGrid && (
        <div className="mt-8">
          <div className="flex justify-between items-center mb-4">
            <h2 className="text-lg font-semibold">{selectedLot.name} - Spot Layout</h2>
            <button
              onClick={handleAddSpot}
              className="inline-flex items-center justify-center rounded-md bg-blue-600 px-3 py-2 text-sm font-semibold text-white shadow-sm hover:bg-blue-500"
            >
              <PlusIcon className="-ml-0.5 mr-1.5 h-5 w-5" />
              Add Spot
            </button>
          </div>
          <ParkingSpotGrid
            spots={selectedLot.spots}
            onSpotSelect={handleSpotUpdate}
            isManager={true}
          />
        </div>
      )}

      <AddEditLotModal
        isOpen={showAddEditModal}
        onClose={() => setShowAddEditModal(false)}
        lot={editingLot}
      />

      <SpotManagementModal
        isOpen={showSpotModal}
        onClose={() => setShowSpotModal(false)}
        spot={selectedSpot}
      />
    </div>
  );
}