const express = require('express');

const cors = require('cors');

const routeRoutes =
  require('./routes/routeRoutes');

const app = express();

app.use(cors());

app.use(express.json());

app.use('/routes', routeRoutes);

app.listen(9001, () =>
  console.log(
    "Logistics Service corriendo en puerto 9001"
  )
);