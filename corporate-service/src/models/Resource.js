const mongoose = require('mongoose');

const ResourceSchema = new mongoose.Schema({

  name: {
    type: String,
    required: true
  },

  type: String,

  availability: {
    type: Boolean,
    default: true
  }

}, { timestamps: true });

module.exports =
  mongoose.model(
    'Resource',
    ResourceSchema
  );