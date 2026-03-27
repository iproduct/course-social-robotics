const ws = new WebSocket("ws://localhost:5000/ws");
const messages = document.getElementById('messages')
ws.onmessage = function (message) {
    const event = JSON.parse(message.data)
    if (event.type === 'command_ack') {
        var li = document.createElement('li')
        var content = document.createTextNode(event.payload)
        li.appendChild(content)
        messages.appendChild(li)
    } 
    // else if (event.type === 'distance') {
    //     distances.innerHTML += `${Math.round(event.distance)}, `
    // } else if (event.type === 'move') {
    //     speeds.innerHTML = `encoderL: ${event.encoderL}, encoderR: ${event.encoderR}, speedL: ${event.speedL}, speedR: ${event.speedR}`
    // }
};
function sendMessage(event) {
    const input = document.getElementById("messageText")
    ws.send(input.value)
    // input.value = ''
    event.preventDefault()
}
