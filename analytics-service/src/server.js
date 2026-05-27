const express = require('express');

const cors = require('cors');

const mongoose = require('mongoose');

const analyticsRoutes =
  require('./routes/analyticsRoutes');

const app = express();

app.use(cors());

app.use(express.json());

mongoose.connect(
  'mongodb://localhost:27017/analytics'
);

app.use('/analytics', analyticsRoutes);

app.listen(8001, () =>
  console.log(
    "Analytics Service corriendo en puerto 8001"
  )
);