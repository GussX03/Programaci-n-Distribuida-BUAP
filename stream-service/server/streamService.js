const express = require('express');

const http = require('http');

const socketIo = require('socket.io');

const cors = require('cors');

const app = express();

app.use(cors());

const server =
  http.createServer(app);

const io = socketIo(server, {

  cors: {
    origin: "*"
  }
});

setInterval(() => {

  const data = {

    temperature:
      (
        20 + Math.random() * 10
      ).toFixed(2)
  };

  io.emit('data', data);

}, 2000);

server.listen(4005, () =>

  console.log(
    "Stream Service en puerto 4005"
  )
);