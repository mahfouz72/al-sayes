import { useState, useEffect } from 'react';
import ReservationList from './ReservationList';
import useAuthStore from '../../store/authStore';

// Mock data - replace with actual API calls
const mockDriverReservations = [
  {
    id: 1,
    parkingLot: 'Downtown Parking',
    spotNumber: 'A12',
    startTime: '2024-03-15T10:00:00',
    duration: 2,
    status: 'Active',
    total: 10.00
  },
  {
    id: 2,
    parkingLot: 'Mall Parking',
    spotNumber: 'B15',
    startTime: '2024-03-14T14:00:00',
    duration: 3,
    status: 'Completed',
    total: 15.00
  }
];

export default function DriverReservations() {
  const [reservations, setReservations] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const { user } = useAuthStore();

  useEffect(() => {
    // Simulate API call
    const fetchReservations = async () => {
      try {
        // Replace with actual API call
        await new Promise(resolve => setTimeout(resolve, 1000));
        setReservations(mockDriverReservations);
      } catch (error) {
        console.error('Error fetching reservations:', error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchReservations();
  }, [user.id]);

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="mb-8">
        <h1 className="text-2xl font-bold text-gray-900">My Reservations</h1>
        <p className="mt-2 text-gray-600">View and manage your parking reservations</p>
      </div>

      <ReservationList
        reservations={reservations}
        isLoading={isLoading}
      />
    </div>
  );
}