const mongoose = require('mongoose');

const ScheduleSchema = new mongoose.Schema({

  employeeId: {

    type: mongoose.Schema.Types.ObjectId,

    ref: 'Employee',

    required: true
  },

  day: String,

  startHour: String,

  endHour: String

}, { timestamps: true });

module.exports =
  mongoose.model(
    'Schedule',
    ScheduleSchema
  );