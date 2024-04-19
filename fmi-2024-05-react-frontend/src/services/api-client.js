import { Subject } from "rxjs";

class ApiClient {
    constructor(apiBaseUrl, wsBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
        this.wsBaseUrl = wsBaseUrl;
    }

    async init() {
        this.ws = new WebSocket(this.wsBaseUrl);
        this.readings = new Subject();
        this.ws.onmessage = (message) => {
            // console.log(message.data);
            const event = JSON.parse(message.data);
            // console.log(this.readings);
            this.readings.next(event);
        };
    }

    sendMessage(event) {
        var input = document.getElementById("messageText");
        this.ws.send(input.value);
    }

    async findAllSensors() {
       const resp =  await fetch(`${this.apiBaseUrl}/sensors`);
       return resp.json();
    }
} 

const API = new ApiClient('http://localhost:5000/api', "ws://localhost:5000/ws");
API.init();

export default API;