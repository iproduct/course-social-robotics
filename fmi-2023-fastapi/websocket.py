import asyncio
import time
from concurrent.futures import ProcessPoolExecutor
from contextlib import asynccontextmanager

import uvicorn
from fastapi import FastAPI, WebSocket
from fastapi.responses import HTMLResponse

import logging

import aiocoap
import aiocoap.resource as resource

SERVER_IP='192.168.1.100'
WEBAPP_PORT=5000
ROBOT_IP='192.168.1.101'

logging.basicConfig(level=logging.INFO)
logging.getLogger("coap-server").setLevel(logging.INFO)

coap_ctx: aiocoap.Context = None
ws: WebSocket = None

@asynccontextmanager
async def lifespan(app: FastAPI):
    global coap_ctx
    coap_ctx = await start_coap_server()
    # coap_ctx = await aiocoap.Context.create_client_context()
    yield
    await coap_ctx.shutdown()


app = FastAPI(lifespan=lifespan, debug=True)

html = """
<!DOCTYPE html>
<html>
    <head>
        <title>Chat</title>
    </head>
    <body>
        <h1>WebSocket Chat</h1>
        <form action="" onsubmit="sendMessage(event)">
            <input type="text" id="messageText" style="width:80%" />
            <button>Send</button>
        </form>
        <div>Distances: <span id="distances"></span></div>
        <div>Speeds: <span id="speeds"></span></div>
        <ul id='messages'>
        </ul>
        <script>
            var ws = new WebSocket("ws://localhost:5000/ws");
            var distances = document.getElementById('distances')
            var speeds = document.getElementById('speeds')
            var messages = document.getElementById('messages')
            ws.onmessage = function(message) {
                const event = JSON.parse(message.data)
                if(event.type === 'command_ack') {
                    var li = document.createElement('li')
                    var content = document.createTextNode(event.payload)
                    li.appendChild(content)
                    messages.appendChild(li)
                } else if(event.type === 'distance') {
                    distances.innerHTML = `L: ${Math.round(event.distanceL)}, R: ${Math.round(event.distanceR)}`
                } else if(event.type === 'move') {
                    speeds.innerHTML = `encoderL: ${event.encoderL}, encoderR: ${event.encoderR}, speedL: ${event.speedL}, speedR: ${event.speedR}`
                }
            };
            function sendMessage(event) {
                var input = document.getElementById("messageText")
                ws.send(input.value)
                // input.value = ''
                event.preventDefault()
            }
        </script>
    </body>
</html>
"""


@app.get("/")
async def get():
    return HTMLResponse(html)


@app.get("/hello/{name}")
async def say_hello(name: str):
    return {"message": f"Hello {name}"}


@app.websocket("/ws")
async def websocket_endpoint(websocket: WebSocket):
    await websocket.accept()
    global ws
    ws = websocket
    while True:
        message = await websocket.receive_text()
        start_time = time.time()
        resp = await send_command(message)
        end_time = time.time()
        coap_ctx.log.info(f'CoAP request time: {(end_time - start_time) * 1000} ms')
        await websocket.send_json({'type': 'command_ack', 'payload': resp})


class BlockResource(resource.Resource):
    """Example resource which supports the GET and PUT methods. It sends large
    responses, which trigger blockwise transfer."""

    def __init__(self):
        super().__init__()
        self.set_content(b"This is the resource's default content. It is padded "
                b"with numbers to be large enough to trigger blockwise "
                b"transfer.\n")

    def set_content(self, content):
        self.content = content

    async def render_get(self, request):
        return aiocoap.Message(payload=self.content)

    async def render_put(self, request):
        print('PUT payload: %s' % request.payload)
        self.set_content(request.payload)
        global ws
        print(ws)
        if ws is not None:
            await ws.send_text(self.content.decode('utf8'))
        return aiocoap.Message(code=aiocoap.CHANGED, payload=self.content)


async def start_coap_server():
    # Resource tree creation
    root = resource.Site()

    root.add_resource(['.well-known', 'core'], resource.WKCResource(root.get_resources_as_linkheader))
    root.add_resource(['sensors', 'distance'], BlockResource())
    return await aiocoap.Context.create_server_context(root, bind=[SERVER_IP, 5683])


async def send_command(message):
    print(f'Sending to ESP32: {message}')
    req = aiocoap.Message(code=aiocoap.PUT, payload=message.encode(encoding='utf-8'), uri='coap://' + ROBOT_IP + ':5683/light')

    try:
        response = await coap_ctx.request(req).response
    except Exception as e:
        print('Failed to fetch resource:')
        print(e)
    else:
        print('Result: %s\n%r' % (response.code, response.payload.decode('utf8')))
        return response.payload.decode('utf8')

if __name__ == "__main__":
    uvicorn.run("websocket:app", port=WEBAPP_PORT, access_log=False, workers=1)