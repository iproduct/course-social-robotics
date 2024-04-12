
import './App.css';
import { useEffect, useRef, useState } from 'react';
import SensorView from './components/SensorView';
import { Sensor } from './models/sensor-model';

function getReading() {
  const now =  Date.now();
  return {id: now, timestamp: now, value: Math.random() * 50000 / 100}
}

function App() {
  const [sensors, setSensors] = useState([ 
    new Sensor('DS_001', 'Front left US sensor of the robot'),
    new Sensor('DS_002', 'Front right US sensor of the robot'),
  ])
  const interval = useRef(undefined);
  useEffect(()=>{
    interval.current = setInterval(() => {
      setSensors(srs => {
        const [s0, s1, ...otherSensors] = srs;
        return [
          {...s0, readings: s0.readings.concat(getReading())},
          {...s1, readings: s1.readings.concat(getReading())},
          ...otherSensors];
      })
    }, 3000)
    return () => {
      clearInterval(interval.current)
    }
  }, []);
  return (
    <div className="container">
      <h1>IoT First Demo</h1>
      {sensors.map(sr => (<SensorView key={sr.id} sensor={sr} />))}
    </div>
  );
}

export default App;
