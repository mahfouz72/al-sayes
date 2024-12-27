import axios from 'axios';
import { config, getUserToken } from './config';

const getParkingSpots = async (lotId) => {
  try {
    const token = getUserToken();
    let headers = {
      'Authorization': `Bearer ${token}`
    };
    const response = await axios.get(`${config.BASE_API_URL}/spots/${lotId}/get`, { headers });
    return response.data;
  } catch (error) {
    console.error('Error fetching parking spots:', error);
    throw error;
  }
};

const updateParkingSpot = async(spot) => {
  try {
    const token = getUserToken();
    let headers = {
      'Authorization': `Bearer ${token}`
    };
    await axios.put(`${config.BASE_API_URL}/spots/update`, spot, { headers } );
  } catch (error) {
    console.error('Error fetching parking spots:', error);
    throw error;
  }
}
export default {
  getParkingSpots,updateParkingSpot
};