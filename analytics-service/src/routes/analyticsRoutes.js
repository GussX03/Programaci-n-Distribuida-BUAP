const express = require('express');

const AnalyticsController =
  require('../controllers/AnalyticsController');

const router = express.Router();

router.post(
  '/sales',
  AnalyticsController.addSale
);

router.get(
  '/sales',
  AnalyticsController.listSales
);

router.get(
  '/stats/monthly',
  AnalyticsController.monthlyStats
);

router.get(
  '/stats/top-products',
  AnalyticsController.topProducts
);

module.exports = router;