const express = require('express');

const RouteController =
  require('../controllers/RouteController');

const router = express.Router();

router.post(
  '/connection',
  RouteController.addConnection
);

router.post(
  '/calculate',
  RouteController.calculateRoute
);

module.exports = router;