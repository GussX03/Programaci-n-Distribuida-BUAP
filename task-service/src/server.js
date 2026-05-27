const express = require('express');

const cors = require('cors');

const mongoose = require('mongoose');

const taskRoutes =
  require('./routes/taskRoutes');

const app = express();

app.use(cors());

app.use(express.json());

mongoose.connect(
  'mongodb://localhost:27017/tasks'
);

app.use('/tasks', taskRoutes);

app.listen(6001, () =>

  console.log(
    "Task Service corriendo en puerto 6001"
  )
);