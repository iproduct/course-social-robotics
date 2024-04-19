
class ApiClient {
    constructor(apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
    }

    async findAllSensors() {
       const resp =  await fetch(`${this.apiBaseUrl}/api/sensors`);
       return resp.json();
    }
} 

const API = new ApiClient('http://localhost:5000');

export default API;