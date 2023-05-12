import json
from datetime import datetime
from flask import Flask, jsonify, make_response, request
from pymongo import MongoClient
from bson.json_util import dumps, ObjectId

app = Flask(__name__)

client = MongoClient('localhost', 27017)
db = client.robot
events_db = db.events

class JSONEncoder(json.JSONEncoder):
    def default(self, o):
        if isinstance(o, ObjectId):
            return str(o)
        return json.JSONEncoder.default(self, o)

encoder = JSONEncoder()

@app.route('/api/events', methods=['GET'])
def get_events():  # put application's code here
    events = list(events_db.find())
    response = make_response(
        encoder.encode(events),
        200,
    )
    response.headers["Content-Type"] = "application/json"
    return response

@app.route('/api/events/latest', methods=['GET'])
def get_latest_event():  # put application's code here
    latest_event = events_db.find().skip(events_db.estimated_document_count() - 1).next()
    response = make_response(
        encoder.encode(latest_event),
        200,
    )
    response.headers["Content-Type"] = "application/json"
    return response


@app.route('/api/events', methods=['POST'])
def post_event():  # put application's code here
    dt = datetime.now()
    event = json.loads(request.data)
    events_db.insert_one(event)
    encoded = encoder.encode(event)
    print(encoded)
    response = make_response(
        encoded,
        201,
    )
    response.headers["Content-Type"] = "application/json"
    return response

if __name__ == '__main__':
    app.run(host='192.168.1.102', port=8080, debug=True)
