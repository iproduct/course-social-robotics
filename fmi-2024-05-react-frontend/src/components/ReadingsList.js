import React from "react";
import Reading from "./Reading";

export default function ReadingsList({readings}) {
  return (
    <ul>
      {readings.map((rd) => (
        <Reading key={rd.id} reading={rd} />
      ))}
    </ul>
  );
}
