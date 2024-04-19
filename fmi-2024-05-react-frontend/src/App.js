
import './App.css';
import { useEffect, useRef, useState } from 'react';
import SensorView from './components/SensorView';

import API from './services/api-client';

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
            console.log(reading)
            setSensors(ssrs => {
              console.log(ssrs)
              const sid = reading.sid;
              const ssr = ssrs.find(sr => sr.id === sid);
              const otherSensors = ssrs.filter(sr => sr.id !== sid)
              return [...otherSensors, { ...ssr, readings: [...ssr.readings, reading] }]
            });
          }
        }
      )
    })() // IIFE
    return  () => readings.current.unsubscribe()
  }, []);
  return (
    <div className="container">
      <h1>IoT First Demo</h1>
      {sensors.map(sr => (<SensorView key={sr.id} sensor={sr} />))}
    </div>
  );
}

export default App;
