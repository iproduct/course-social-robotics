import React, { useEffect, useRef, useState } from 'react';
import './App.css';
import API_CLIENT from './api-client';
import { clearScreenDown } from 'readline';

interface EventData {
  _id: string;
  time: number;
  distanceL: number;
  distanceR: number;
}

function App() {
  const [distLeft, setDistLeft] = useState<EventData>();
  const [errors, setErrors] = useState<Error>();
  const interval = useRef<NodeJS.Timer>()

  useEffect(() => {
    interval.current = setInterval(() => {
      API_CLIENT.getLatestData().then(
        latest => {
          setDistLeft(latest)
          setErrors(undefined)
        }).catch(err => {
          console.log(`ERROR Fetching API data: ${err}`)
          setErrors(err.message)
        })
      return () => {
        clearInterval(interval.current)
      }
    }, 500)
  }, [])

  return (
    <div className="App">
      <header className="App-header">
        <h2>Distance - Left sensor: {distLeft?.distanceL}, Right sensor: {distLeft?.distanceR} in {distLeft?.time} </h2>
        {errors && (<h3>Error: {JSON.stringify(errors)}</h3>)}
      </header>
    </div>
  );
}

export default App;
