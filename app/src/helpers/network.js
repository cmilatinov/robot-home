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

    post(url, params) {
        return this.axiosInstance.post(url, params);
    }

}

export default new Network();