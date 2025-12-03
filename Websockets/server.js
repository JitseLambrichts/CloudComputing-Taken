// Werkt nu met JSON omdat de WebSocket nu moet weten of deze eem bericht moet verzenden of de user moet updaten

const WebSocket = require("ws");
const wss = new WebSocket.Server({ port: 3001 });

var clientnr = 1;
var clientData = new Map();

wss.on("connection", (ws) => {
  clientData.set(ws, {
    id: clientnr,
    date: new Date(),
    username: null,
    color: "black",
  });

  // console.log("A new client");
  ws.send("Welcome to my websocket ");

  wss.clients.forEach((client) => {
    client.send("Client " + clientnr + " just joined");
  });

  clientnr += 1;

  ws.on("message", (msg) => {
    const data = JSON.parse(msg);
    let metadata = clientData.get(ws);

    if (data.type === "username") {
      metadata.username = data.content;
      clientData.set(ws, metadata);
    } else if (data.type === "color") {
      metadata.color = data.content;
      clientData.set(ws, metadata);
      // console.log("Color updated to: " + data.content);
    } else if (data.type === "message") {
      let senderName = metadata.username ? metadata.username : "Client " + metadata.id;
      // console.log("Sending message with color: " + metadata.color);

      wss.clients.forEach((client) => {
        if (ws != client && client.readyState == WebSocket.OPEN) {
          client.send("<div style=\"color:" + metadata.color + "\">" + senderName + ": " + data.content + "</div>");
        }
      });
      // console.log(data.content + " received");
    }
  });

  ws.on("close", (evt) => {
    clientnr--;
    wss.clients.forEach((client) => {
      if (ws != client && client.readyState == WebSocket.OPEN) {
        client.send("We now have " + clientnr + " active clients");
      }
    });
  });
});
