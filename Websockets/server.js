// Werkt nu met JSON omdat de WebSocket nu moet weten of deze eem bericht moet verzenden of de user moet updaten

const WebSocket = require("ws");
const wss = new WebSocket.Server({ port: 3001 });

var clientnr = 0;
var clientData = new Map();

wss.on("connection", (ws) => {
  clientnr += 1;

  // Standaard initialisatie voor de nieuwe users
  clientData.set(ws, {
    id: clientnr,
    date: new Date(),
    username: null,
    color: "black",
  });

  ws.send("Welcome to my websocket ");

  wss.clients.forEach((client) => {
    client.send("Client " + clientnr + " just joined");
  });

  // De lijst van de actieve gebruikers updaten voor als er iemand joined
  updateUserList();

  ws.on("message", (msg) => {
    const data = JSON.parse(msg);
    let metadata = clientData.get(ws);

    // Verschillende if-checks om te kijken wat voor type data is -> als de naam wordt aangepast zal de data een username zijn, als de kleur wordt aangepast zal de data een kleur zijn etc...
    if (data.type === "username") {
      metadata.username = data.content;
      clientData.set(ws, metadata);
      updateUserList();
    } else if (data.type === "color") {
      metadata.color = data.content;
      clientData.set(ws, metadata);
      updateUserList();
    } else if (data.type === "private_message") {
      let senderName = metadata.username ? metadata.username : "Client: " + metadata.id;
      let targetUsername = data.target;

      wss.clients.forEach((client) => {
        let clientMeta = clientData.get(client);

        if (clientMeta && (clientMeta.username === targetUsername || ("Client " + clientMeta.id) === targetUsername)) {
          if (client.readyState == WebSocket.OPEN) {
            client.send("<div style='color:purple; font-style:italic;'> (Privé van " + senderName + "): " + data.content + "</div>");
          }
        }
      });
    } else if (data.type === "message") {
      let senderName = metadata.username ? metadata.username : "Client " + metadata.id;

      wss.clients.forEach((client) => {
        if (ws != client && client.readyState == WebSocket.OPEN) {
          client.send("<div style=\"color:" + metadata.color + "\">" + senderName + ": " + data.content + "</div>");
        }
      });
    }
  });

  // Verbinding sluiten
  ws.on("close", () => {
    clientnr--;
    wss.clients.forEach((client) => {
      if (ws != client && client.readyState == WebSocket.OPEN) {
        client.send("We now have " + clientnr + " active clients");
      }
    });
    updateUserList();
  });
});

// Users "doorsturen" zodat deze kunnen worden weergegeven
function updateUserList() {
  let users = [];

  wss.clients.forEach((client => {
    let meta = clientData.get(client);
    if (meta && client.readyState == WebSocket.OPEN) {
      users.push({
        name: meta.username ? meta.username : "Client " + meta.id,
        color: meta.color
      });
    }
  }));

  let message = JSON.stringify({
    type: "users_list",           // Hier dus het type doorgeven om te checken wat de websocket moet doen
    users: users
  });
  
  wss.clients.forEach((client) => {
    if (client.readyState == WebSocket.OPEN) {
      client.send(message);
    }
  })
}
