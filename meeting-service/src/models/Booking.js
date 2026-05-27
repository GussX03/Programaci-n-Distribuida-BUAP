const mongoose = require('mongoose');

const BookingSchema = new mongoose.Schema({
  room: { type: String, required: true },
  user: { type: String, required: true },
  startTime: { type: Date, required: true },
  endTime: { type: Date, required: true },
  status: { type: String, enum: ['reserved', 'cancelled'], default: 'reserved' }
}, { timestamps: true });

module.exports = mongoose.model('Booking', BookingSchema);