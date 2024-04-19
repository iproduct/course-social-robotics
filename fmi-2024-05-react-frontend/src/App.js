
import './App.css';
import { useEffect, useRef, useState } from 'react';
import SensorView from './components/SensorView';

import API from './services/api-client';
import { CommandInput } from './components/CommandInput';

function App() {
  const [sensors, setSensors] = useState([]);
  const readings = useRef();
  useEffect(() => {
    (async () => {
      const ssors = (await API.findAllSensors()).map(sr => ({ ...sr, readings: [] }));
      setSensors(ssors);
    })() // IIFE
  }, []);

  useEffect(() => {
    (async () => {
      readings.current = API.readings.subscribe(
        {
          next: (reading) => {
            setSensors(ssrs => ssrs.map(ssr => ssr.id === reading.sid ?
              { ...ssr, readings: [...ssr.readings, reading] }
              : ssr)
            );
          }
        }
      )
    })() // IIFE
    return () => readings.current.unsubscribe()
  }, []);

  const doCommand = (cmd) => {
    console.log(cmd);
    API.sendMessage(cmd);
  }
  return (
    <div className="container">
      <h1>IoT First Demo</h1>
      <CommandInput onCommnand={doCommand} />
      {sensors.map(sr => (<SensorView key={sr.id} sensor={sr} />))}
    </div>
  );
}

export default App;
