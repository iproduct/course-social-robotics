
class ApiClient {
    async getLatestData() {
        const resp = await fetch('http://192.168.1.102:8080/api/events/latest')
        const latest = await resp.json()
        return latest
    }
}

const API_CLIENT = new ApiClient()

export default API_CLIENT