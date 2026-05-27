const mongoose = require('mongoose');

const EmployeeSchema = new mongoose.Schema({

  name: {
    type: String,
    required: true
  },

  department: String,

  role: String

}, { timestamps: true });

module.exports =
  mongoose.model(
    'Employee',
    EmployeeSchema
  );