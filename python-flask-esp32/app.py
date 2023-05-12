import json
import uuid
from datetime import datetime
from flask import Flask, jsonify, make_response, request
import atomics
from pymongo import MongoClient
from bson.json_util import dumps, ObjectId
from copy import copy

app = Flask(__name__)

client = MongoClient('localhost', 27017)
db = client.robot
events_db = db.events

# events = []
next_id = atomics.atomic(width=4, atype=atomics.INT)

class JSONEncoder(json.JSONEncoder):
    def default(self, o):
        if isinstance(o, ObjectId):
            return str(o)
        return json.JSONEncoder.default(self, o)

@app.route('/')
def hello_world():  # put application's code here
    return 'Hello World!'


@app.route('/api/events/sample', methods=['GET'])
def get_events_sample():  # put application's code here
    dt = datetime.now()
    response = make_response(
        jsonify(
            {"timestamp": dt, "distance": 25}
        ),
        200,
    )
    response.headers["Content-Type"] = "application/json"
    return response


@app.route('/api/events/new', methods=['GET'])
def add_event_get_args():  # put application's code here
    dt = datetime.now()
    args = request.args
    response = make_response(
        jsonify(
            {"timestamp": dt, "distance": args}
        ),
        200,
    )
    response.headers["Content-Type"] = "application/json"
    return response


@app.route('/api/events', methods=['GET'])
def get_events():  # put application's code here
    events = list(events_db.find())
    response = make_response(
        JSONEncoder().encode(events),
        200,
    )
    response.headers["Content-Type"] = "application/json"
    return response


@app.route('/api/events', methods=['POST'])
def post_event():  # put application's code here
    dt = datetime.now()
    event = json.loads(request.data)
    # event['id'] = next_id.fetch_inc()
    print(event)
    events_db.insert_one(event)
    print(event)
    response = make_response(
        jsonify(
            {"status": "created", "event": JSONEncoder().encode(event)}
        ),
        201,
    )
    response.headers["Content-Type"] = "application/json"
    return response

if __name__ == '__main__':
    app.run(host='192.168.1.102', port=8080, debug=True)
