import React, { useState } from 'react'

export const CommandInput = ({ onCommnand }) => {
    const [commandText, setCommandText] = useState('');
    function submitCommand(event) {
        event.preventDefault();
        onCommnand(commandText)
    }
    return (
        <form onSubmit={submitCommand}>
            <input type="text" value={commandText} onChange={(event) => setCommandText(event.target.value)} />
            <button type="submit">Submit</button>
            <div>{commandText}</div>
        </form>
    )
}
