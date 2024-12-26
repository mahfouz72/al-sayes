import axios from 'axios';
import { config, getUserToken } from './config';

const getParkingLots = async () => {
  try {
    const token = getUserToken();
    let headers = {
      'Authorization': `Bearer ${token}`
    };
    const response = await axios.get(`${config.BASE_API_URL}/lots/get`, { headers });
    console.log(response);
    return response.data;
  } catch (error) {
    console.error('Error fetching parking lots:', error);
    throw error;
  }
};

export default {
  getParkingLots,
};