import axios from 'axios';
import config from './config';

const getParkingLots = async () => {
  try {
    const response = await axios.get(`${config.BASE_API_URL}/lots/get`);
    return response.data;
  } catch (error) {
    console.error('Error fetching parking lots:', error);
    throw error;
  }
};

export default {
  getParkingLots,
};