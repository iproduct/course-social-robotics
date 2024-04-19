
import './App.css';
import { useEffect, useRef, useState } from 'react';
import SensorView from './components/SensorView';

import API from './services/api-client';

function App() {
  const [sensors, setSensors] = useState([]);
  useEffect(()=>{
      (async () => {
         const ssors= (await API.findAllSensors()).map(sr => ({...sr, readings: []}));
         setSensors(ssors);
      })() // IIFE
  }, []);
  return (
    <div className="container">
      <h1>IoT First Demo</h1>
      {sensors.map(sr => (<SensorView key={sr.id} sensor={sr} />))}
    </div>
  );
}

export default App;
