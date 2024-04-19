
class ApiClient {
    constructor(apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
    }

    async findAllSensors() {
       const resp =  await fetch(`${this.apiBaseUrl}/api/sensors`);
       return resp.json();
    }
} 