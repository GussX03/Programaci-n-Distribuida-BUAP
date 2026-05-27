const express = require('express');

const BookingController =
  require('../controllers/BookingController');

const router = express.Router();

router.post('/', BookingController.create);

router.get('/', BookingController.list);

router.put('/:id/cancel', BookingController.cancel);

module.exports = router;