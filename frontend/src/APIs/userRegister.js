import axios from 'axios';
import { BACKEND_BASE, REGISTER_API } from '../constants/Constants';

axios.defaults.baseURL = BACKEND_BASE;

const registerUser = async (userData) => {
    try {
        const response = await axios.post(REGISTER_API, userData);
        return response;

    } catch (error) {
        throw error;
    }
};

export { registerUser };