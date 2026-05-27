const express = require('express');

const cors = require('cors');

const mongoose = require('mongoose');

const bookingRoutes =
  require('./routes/bookingRoutes');

const app = express();

app.use(cors());

app.use(express.json());

mongoose.connect('mongodb://localhost:27017/meeting');

app.use('/bookings', bookingRoutes);

app.listen(7001, () =>
  console.log(
    "Meeting Service corriendo en puerto 7001"
  )
);