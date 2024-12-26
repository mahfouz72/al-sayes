import { useEffect, useState } from 'react';
import { PlusIcon, PencilIcon, TrashIcon } from '@heroicons/react/24/outline';
import ParkingSpotGrid from './ParkingSpotGrid';
import AddEditLotModal from './modals/AddEditLotModal';
import SpotManagementModal from './modals/SpotManagementModal';
import LotAPI from '../../apis/LotAPI';
import SpotAPI from '../../apis/SpotAPI';

export default function ManageLots() {
  const [parkingLots, setParkingLots] = useState([]);
  const [selectedLot, setSelectedLot] = useState(null);
  const [showSpotGrid, setShowSpotGrid] = useState(false);
  const [showAddEditModal, setShowAddEditModal] = useState(false);
  const [showSpotModal, setShowSpotModal] = useState(false);
  const [selectedSpot, setSelectedSpot] = useState(null);
  const [editingLot, setEditingLot] = useState(null);
  const [selectedLotSpots, setSelectedLotSpots] = useState([])  // Store spots for selected lot

  useEffect(() => {
    // Fetch all parking lots when the page first renders
    LotAPI.getParkingLotsCardsViewInManager()
      .then(response => {
        console.log(response);
        const responseConverted = response.map( lot => ({
          id: lot.id,
          name: lot.name,
          location: lot.location,
          timeLimit: 0, // Assuming default value as it's not present in response
          automaticReleaseTime: 0, // Assuming default value as it's not present in response
          notShowingUpPenalty: 0, // Assuming default value as it's not present in response
          overTimeScale: 0, // Assuming default value as it's not present in response,
          parkingTypes: {
            REGULAR: { capacity: lot.regularSpots, basePricePerHour: 0 },
            EV_CHARGING: { capacity: lot.evSpots, basePricePerHour: 0 },
            DISABLED: { capacity: lot.disabledSpots, basePricePerHour: 0 }
          },
          averagePrice: lot.averagePrice
        }));
        setParkingLots(responseConverted);
      })
      .catch(error => {
        console.error('There was an error fetching the parking lots data:', error)
      })
  }, [])

  // Fetch parking spots when selected lot changes
  useEffect(() => {
    if (selectedLot) {
      SpotAPI.getParkingSpots(selectedLot.id)
        .then(response => {
          setSelectedLotSpots(response)
        })
        .catch(error => {
          console.error('Error fetching parking spots:', error)
        })
    }
  }, [selectedLot])

  const handleAddLot = () => {
    setEditingLot(null);
    setShowAddEditModal(true);
  };
  const handleSaveLot = (lot, newlyCreated) => {
    if (newlyCreated) {
      setParkingLots([...parkingLots, lot]);
    } else {
      setParkingLots(parkingLots.map(existingLot => 
        existingLot.id === lot.id ? lot : existingLot
      ));
    }
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

  const handleSaveSpot = (spot) => {
    setSelectedLotSpots(selectedLotSpots.map(existingSpot => 
      existingSpot.id === spot.id ? spot : existingSpot
    ));
  };
  const getTotalCapacity = (parkingTypes)  => {
    return Object.values(parkingTypes).reduce((total, type) => total + type.capacity, 0);
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
                      Average Price/Hour
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
                      <td className="whitespace-nowrap px-3 py-4 text-sm text-gray-500">{getTotalCapacity(lot.parkingTypes)}</td>
                      <td className="whitespace-nowrap px-3 py-4 text-sm text-gray-500">${lot.averagePrice}</td>
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
          {/* <div className="flex justify-between items-center mb-4">
            <h2 className="text-lg font-semibold">{selectedLot.name} - Spot Layout</h2>
            <button
              onClick={handleAddSpot}
              className="inline-flex items-center justify-center rounded-md bg-blue-600 px-3 py-2 text-sm font-semibold text-white shadow-sm hover:bg-blue-500"
            >
              <PlusIcon className="-ml-0.5 mr-1.5 h-5 w-5" />
              Add Spot
            </button>
          </div> */}
          <ParkingSpotGrid
            spots={selectedLotSpots}
            onSpotSelect={handleSpotUpdate}
            isManager={true}
          />
        </div>
      )}

      <AddEditLotModal
        isOpen={showAddEditModal}
        onClose={() => setShowAddEditModal(false)}
        onSaveLot={handleSaveLot}
        lot={editingLot}
      />

      <SpotManagementModal
        isOpen={showSpotModal}
        onClose={() => setShowSpotModal(false)}
        spot={selectedSpot}
        onSaveSpot = {handleSaveSpot}
      />
    </div>
  );
}