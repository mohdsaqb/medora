import axios from 'axios';
import AuthService from './AuthService';

// Set REACT_APP_API_URL at build time to point at the deployed backend.
// The fallback only suits local development.
const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8185/api';

const client = axios.create({ baseURL: API_BASE_URL });

// Attach Basic credentials when signed in. Reads are public, so requests
// without a session are still valid and simply go out unauthenticated.
client.interceptors.request.use((config) => {
    const header = AuthService.getAuthHeader();
    if (header) {
        config.headers.Authorization = header;
    }
    return config;
});

// A 401 means the stored credentials are no longer accepted, so clear them
// rather than retrying with the same values on every subsequent request.
client.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response && error.response.status === 401) {
            AuthService.signOut();
        }
        return Promise.reject(error);
    }
);

class ApiService {

    getAllDatas(url) {
        return client.get(url);
    }

    getAll(url) {
        return client.get(url);
    }

    getOneById(url) {
        return client.get(url);
    }

    deleteById(url) {
        return client.delete(url);
    }

    post(url, data) {
        return client.post(url, data);
    }

    put(url, data) {
        return client.put(url, data);
    }
}

const apiService = new ApiService();
export default apiService;
