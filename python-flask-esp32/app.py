from datetime import datetime
from flask import Flask, jsonify, make_response, request

app = Flask(__name__)

events = []

@app.route('/')
def hello_world():  # put application's code here
    return 'Hello World!'

@app.route('/api/events', methods=['GET'])
def get_events():  # put application's code here
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



if __name__ == '__main__':
    app.run()
