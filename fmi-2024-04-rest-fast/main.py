from dataclasses import dataclass, field
from typing import Iterable

import uvicorn
from fastapi import FastAPI
from pydantic import BaseModel

app = FastAPI(debug=True)


class Event(BaseModel):
    value: float
    timestamp: int
    id: int = None


class EventRepository:
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
        event.id = self.get_next_id()
        self.events[event.id] = event
        return event


events_repo = EventRepository()


@app.get("/")
async def root():
    return {"message": "Hello FastAPI"}


@app.get("/hello/{name}")
async def say_hello(name: str):
    return {"message": f"Hello {name}"}


@app.get("/api/events")
async def find_all_events():
    return events_repo.find_all()


@app.post("/api/events")
async def new_event(event: Event):
    return events_repo.create(event)


if __name__ == '__main__':
    uvicorn.run("main:app", host="192.168.1.100", port=9000, reload=True, access_log=False)
