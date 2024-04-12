import React from "react";

export default function Reading({reading: rd}) {
  return (
    <li>
      <b>{rd.value}</b> [{new Date(rd.timestamp).toUTCString()}]
    </li>
  );
}
