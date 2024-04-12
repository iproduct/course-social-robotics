import React from 'react'
import ReadingsList from './ReadingsList'

export default function SensorView({sensor}) {
  return (
    <div>
        <div>{sensor.id}: {sensor.description}</div>
        <ReadingsList readings={sensor.readings} />
    </div>
  )
}
