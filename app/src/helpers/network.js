import axios from 'axios';

class Network {

    axiosInstance;

    constructor(url = process.env.VUE_APP_API_URL) {
        this.axiosInstance = axios.create({
            baseURL: url
        });
    }

    get(url, params) {
        return this.axiosInstance.get(url, params);
    }

    post(url, body, params) {
        return this.axiosInstance.post(url, body, params);
    }

    setToken(token) {
        this.axiosInstance.defaults.headers.common[`Authorization`] = `Bearer ${token}`;
    }

    clearToken() {
        delete this.axiosInstance.defaults.headers.common["Authorization"];
    }

}

export default new Network();