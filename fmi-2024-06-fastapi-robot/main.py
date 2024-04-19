import uvicorn
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

app = FastAPI(debug=True)
sensors = [
    {'id': 'DS_001', 'description': 'Front left US sensor of the robot'},
    {'id': 'DS_002', 'description': 'Front right US sensor of the robot'}
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


if __name__ == "__main__":
    uvicorn.run("main:app", port=5000, reload=True, access_log=False)
