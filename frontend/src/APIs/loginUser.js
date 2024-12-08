import axios from 'axios';
import { BACKEND_BASE, LOGIN_API } from '../constants/Constants';

axios.defaults.baseURL = BACKEND_BASE;

const loginUser = async (userData) => {
    try {
        const response = await axios.post(LOGIN_API, userData);
        return response;

    } catch (error) {
        throw error;
    }
};

export { loginUser };