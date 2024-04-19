import random
import time

import uvicorn
from fastapi import FastAPI, WebSocket
from fastapi.middleware.cors import CORSMiddleware
import asyncio

app = FastAPI(debug=True)
sensors = [
    {'id': 'DS_001', 'description': 'Front left US sensor of the robot'},
    {'id': 'DS_002', 'description': 'Front right US sensor of the robot'},
    {'id': 'DS_003', 'description': 'Front IR sensor of the robot'},
]

origins = [
    "http://localhost:3000",
]

app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


@app.get("/api/sensors")
async def root():
    return sensors


@app.get("/api/sensors/{name}")
async def say_hello(name: str):
    return {"message": f"Hello {name}"}


@app.websocket("/ws")
async def websocket_endpoint(websocket: WebSocket):
    await websocket.accept()
    global ws
    ws = websocket
    id = 0
    while True:
        await asyncio.sleep(3)
        await websocket.send_json({
            'id': id + 1,
            'sid': sensors[id % 3]['id'],
            'timestamp': round(time.time() * 1000),
            'value': random.randint(5, 500)
        })
        id += 1

    # while True:
    #     message = await ws.receive_text()
    #     print(message)
        # start_time = time.time()
        # resp = await send_command(message)
        # end_time = time.time()
        # coap_ctx.log.info(f'CoAP request time: {(end_time - start_time) * 1000} ms')
        # await websocket.send_json({'type': 'command_ack', 'payload': resp})


if __name__ == "__main__":
    uvicorn.run("main:app", port=5000, reload=True, access_log=False)
