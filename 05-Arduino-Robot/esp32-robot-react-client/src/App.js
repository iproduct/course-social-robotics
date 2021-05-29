import './App.css';
import React, { Component } from 'react'
import eventService from './service/event-service';

export default class App extends Component {
  state = {
    messages: [],
    errors: []
  }

  constructor(props) {
    super(props);
    this.moveForward = this.moveForward.bind(this);
  }

  render() {
    return (
      <div className="App">
        <header className="App-header">
          <h2>Robots Events Demo</h2>
          <button onClick={this.moveForward}>Move Forward</button>
          <button onClick={this.moveStop}>Stop</button>

          <div>
            <h3>Events:</h3>
            <ul>
              {
                this.state.messages.map(m => (<li key={m.id}>{JSON.stringify(m)}</li>))
              }
            </ul>
          </div>
          {this.errors?.length &&
            (<div>
              <h3>Errors:</h3>
              <ul>
                {
                  this.state.erorrs.map(err => (<li key={err}>{err}</li>))
                }
              </ul>
            </div>)
          }
        </header>
      </div>
    );
  }

  componentDidMount() {
    eventService.wsSubject.subscribe(
      message => {
        this.showMessage(message);
      },
      error => {
        this.showError(error);
      },
      () => {
        this.showMessage("Event stream completed.");
      });
  }

  showMessage(message) {
    this.setState(state => ({messages: state.messages.concat(message)}));
  }
  showError(error) {
    this.setState(state => ({errors: state.errors.concat(error)}));
  }

  moveForward(){
    eventService.sendEvent("moveForward");
  }
  moveStop = () => {
    eventService.sendEvent("moveStop");
  }
}
