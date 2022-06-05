import time
import uuid

from flask import Flask, jsonify, request, abort

app = Flask(__name__)

events = [
    {
        'id': 1,
        'sensorId': 'US-L',
        'timestamp': time.time(),
        'distance': 10
    },
    {
        'id': 2,
        'sensorId': 'US-R',
        'timestamp': time.time(),
        'distance': 15
    },
    {
        'id': 3,
        'sensorId': 'US-R',
        'timestamp': time.time(),
        'distance': 18
    }
]


@app.route('/api/events', methods=['GET'])
def get_events():
    return jsonify(events)


@app.route('/api/events', methods=['POST'])
def create_event():
    if not request.json or not 'sensorId' in request.json or not 'distance' in request.json:
        abort(400)
    event = {
        'id': uuid.uuid1(),
        'sensorId': request.json['sensorId'],
        'timestamp': request.json['timestamp'],
        'distance': request.json['distance']
    }
    events.append(event)
    response = jsonify(event)
    response.status_code = 201
    response.headers['location'] = f"/api/events/{event['id']}"
    response.autocorrect_location_header = False
    return response


if __name__ == '__main__':
    app.run(host='10.108.4.103')
