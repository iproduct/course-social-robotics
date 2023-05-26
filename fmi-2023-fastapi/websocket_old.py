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

logging.basicConfig(level=logging.INFO)
logging.getLogger("coap-server").setLevel(logging.INFO)

coap_ctx: aiocoap.Context = None
ws: WebSocket = None

# req = Message(code=PUT, payload='', uri='coap://192.168.1.100/other/block')

@asynccontextmanager
async def lifespan(app: FastAPI):
    global coap_ctx
    coap_ctx = await start_coap_server()
    # global ws
    # ws = app.websocket('/ws')
    # coap_ctx = await aiocoap.Context.create_client_context()
    yield
    await coap_ctx.shutdown()
    # await server_ctx.shutdown()


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
            <input type="text" id="messageText" autocomplete="off"/>
            <button>Send</button>
        </form>
        <div>Distances: <span id="distances"></span></div>
        <ul id='messages'>
        </ul>
        <script>
            var ws = new WebSocket("ws://localhost:5000/ws");
            var distances = document.getElementById('distances')
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
                }
            };
            function sendMessage(event) {
                var input = document.getElementById("messageText")
                ws.send(input.value)
                input.value = ''
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
        # while len(self.content) <= 1024:
        #     self.content = self.content + b"0123456789\n"

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
    # root.add_resource(['time'], TimeResource())
    root.add_resource(['sensors', 'distance'], BlockResource())
    # root.add_resource(['other', 'separate'], SeparateLargeResource())
    # root.add_resource(['whoami'], WhoAmI())
    return await aiocoap.Context.create_server_context(root, bind=['192.168.1.100', 5683])


async def send_command(message):
    # ctx = await Context.create_client_context()

    # request = Message(code=GET, uri='coap://192.168.1.100/time')
    # request = Message(code=GET, uri='coap://192.168.1.100/other/block')
    # request = Message(code=GET, uri='coap+ws://192.168.1.100/whoami')
    # req = Message(code=GET, uri='coap+ws://192.168.1.100:8683/light')

    # payload = b'1';

    # req.remote = UndecidedRemote("coap", "192.168.1.101:5683")
    # req.opt.proxy_scheme = 'coap'

    # req = Message(code=PUT, payload=message.encode(encoding='utf-8'), uri='coap://192.168.1.101:5683/light')
    req = aiocoap.Message(code=aiocoap.PUT, payload=message.encode(encoding='utf-8'), uri='coap://192.168.1.101:5683/light')

    try:
        response = await coap_ctx.request(req).response
    except Exception as e:
        print('Failed to fetch resource:')
        print(e)
    else:
        print('Result: %s\n%r' % (response.code, response.payload.decode('utf8')))
        return response.payload.decode('utf8')
    # finally:
    #     await ctx.shutdown()


# async def main():
#     boo_task = asyncio.create_task(start_coap_server())
#
#     config = uvicorn.Config("websocket:app", port=5000, log_level="info", workers=10)
#     server = uvicorn.Server(config)
#     baa_task = asyncio.create_task(server.serve())
#
#     await boo_task
#     await baa_task

if __name__ == "__main__":
    # asyncio.ensure_future(start_coap_server())
    # executor = ProcessPoolExecutor(2)
    # loop = asyncio.new_event_loop()
    # boo = loop.run_in_executor(executor, start_coap_server())
    #

    # baa = loop.run_in_executor(executor, server.serve())
    #
    # loop.run_forever()

    #run("websocket:app", port=5000, access_log=False, workers=1)
    # baa = loop.run_in_executor(executor, say_baa)
    #
    # loop.run_forever()
    #




    # loop = asyncio.get_event_loop()
    # loop.run_forever()

    # asyncio.run(main())
    uvicorn.run("websocket:app", port=5000, access_log=False, workers=1)