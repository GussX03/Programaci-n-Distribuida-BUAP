const Booking = require('../models/Booking');

class BookingService {
  async createBooking(data) {
    const overlap = await Booking.findOne({
      room: data.room,
      startTime: { $lt: data.endTime },
      endTime: { $gt: data.startTime },
      status: 'reserved'
    });

    if (overlap)
      throw new Error("La sala ya está reservada en ese horario");

    const booking = new Booking(data);

    return await booking.save();
  }

  async getBookings() {
    return await Booking.find();
  }

  async cancelBooking(id) {
    return await Booking.findByIdAndUpdate(
      id,
      { status: 'cancelled' },
      { new: true }
    );
  }
}

module.exports = new BookingService();