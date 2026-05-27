const BookingService = require('../services/BookingService');

class BookingController {

  async create(req, res) {
    try {

      const booking =
        await BookingService.createBooking(req.body);

      res.status(201).json(booking);

    } catch (err) {

      res.status(400).json({
        error: err.message
      });

    }
  }

  async list(req, res) {

    const bookings =
      await BookingService.getBookings();

    res.json(bookings);
  }

  async cancel(req, res) {

    try {

      const booking =
        await BookingService.cancelBooking(req.params.id);

      res.json(booking);

    } catch (err) {

      res.status(400).json({
        error: err.message
      });

    }
  }
}

module.exports = new BookingController();