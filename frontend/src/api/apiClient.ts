import axios from 'axios';

const apiClient = axios.create({
  baseURL: 'http://localhost:8080', // hardcoded for now, remember to check it later
  headers: {
    'Content-Type': 'application/json',
  },
});

export default apiClient;