from dataclasses import dataclass, field
from typing import Iterable

import uvicorn
from fastapi import FastAPI
from fastapi.openapi.models import Response
from pydantic import BaseModel
from starlette.responses import JSONResponse
from pymongo import MongoClient
from pymongo.collection import Collection
from bson.json_util import dumps, ObjectId

app = FastAPI(debug=True)


class Event(BaseModel):
    value: float
    timestamp: int
    _id: int = None


class EventRepositoryInMemory:
    next_id = 0

    @classmethod
    def get_next_id(cls):
        cls.next_id += 1
        return cls.next_id

    def __init__(self):
        self.events: dict[int, Event] = dict()

    def find_all(self) -> list[Event]:
        return list(self.events.values())

    def create(self, event: Event) -> Event:
        event._id = self.get_next_id()
        self.events[event._id] = event
        return event


class EventRepositoryMongoDb:
    def __init__(self, client):
        self.db = client.robot_events
        self.events_db: Collection = self.db.events

    def find_all(self) -> list[Event]:
        def transformObjectIdToStr(eventObjId):
            eventObjId["_id"] = str(eventObjId["_id"])
            return eventObjId;
        return list(map(transformObjectIdToStr, self.events_db.find()))

    def create(self, event: Event) -> Event:
        result = self.events_db.insert_one(event.__dict__)
        if result.acknowledged:
            return event
        return None


client = MongoClient('localhost', 27017)
events_repo = EventRepositoryMongoDb(client)


@app.get("/")
async def root():
    return {"message": "Hello FastAPI"}


@app.get("/hello/{name}")
async def say_hello(name: str):
    return {"message": f"Hello {name}"}


@app.get("/api/events")
async def find_all_events():
    return events_repo.find_all()


@app.post("/api/events", status_code=201)
async def new_event(event: Event):
    created = events_repo.create(event)
    return JSONResponse(content=created.json(), headers={'Location': f"/api/events/{created._id}"})


if __name__ == '__main__':
    uvicorn.run("main:app", host="192.168.1.100", port=9000, reload=True, access_log=False)
