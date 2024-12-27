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

const getParkingLotsCardsViewInManager = async () => {
  try {
    const token = getUserToken();
    let headers = {
      'Authorization': `Bearer ${token}`
    };
    const response = await axios.get(`${config.BASE_API_URL}/lots/get/cards`, { headers });
    console.log(response);
    return response.data;
  } catch (error) {
    console.error('Error fetching parking lots:', error);
    throw error;
  }
};

const handleCreateNewLot = async (payload) => {
  try {
    const token = getUserToken();
    let headers = {
      'Authorization': `Bearer ${token}`
    };
    const response =  await axios.post(`${config.BASE_API_URL}/lots/create`, payload, { headers });
    return response.data; // the inserted id
  } catch (error) {
    console.error('Error creating new lot:', error);
    throw error;
  }
};

// Update an existing lot
const handleUpdateLot = async (payload) => {
  try {
    const token = getUserToken();
    let headers = {
      'Authorization': `Bearer ${token}`
    };
    await axios.put(`${config.BASE_API_URL}/lots/update`, payload, { headers });
    return;
  } catch (error) {
    console.error('Error creating new lot:', error);
    throw error;
  }
};

// Update an existing lot
const handleDeleteLot = async (lotId) => {
  try {
    const token = getUserToken();
    let headers = {
      'Authorization': `Bearer ${token}`
    };
    await axios.delete(`${config.BASE_API_URL}/lots/delete/${lotId}`, { headers });
    return;
  } catch (error) {
    console.error('Error deleting lot:', error);
    throw error;
  }
};

export default {
  getParkingLots, handleCreateNewLot, handleUpdateLot, getParkingLotsCardsViewInManager, handleDeleteLot
};