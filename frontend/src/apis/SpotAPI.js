import axios from 'axios';
import config from './config';

const getParkingSpots = async (lotId) => {
  try {
    const response = await axios.get(`${config.BASE_API_URL}/spots/${lotId}/get`);
    return response.data;
  } catch (error) {
    console.error('Error fetching parking spots:', error);
    throw error;
  }
};

export default {
  getParkingSpots,
};