const express = require('express');

const cors = require('cors');

const mongoose = require('mongoose');

const corporateRoutes =
  require('./routes/corporateRoutes');

const app = express();

app.use(cors());

app.use(express.json());

mongoose.connect(
  'mongodb://localhost:27017/corporate'
);

app.use('/corporate', corporateRoutes);

app.listen(10001, () =>

  console.log(
    "Corporate Service corriendo en puerto 10001"
  )
);