const express = require('express');

const CorporateController =
  require('../controllers/CorporateController');

const router = express.Router();

router.post(
  '/employees',
  CorporateController.addEmployee
);

router.get(
  '/employees',
  CorporateController.listEmployees
);

router.post(
  '/schedules',
  CorporateController.assignSchedule
);

router.get(
  '/schedules',
  CorporateController.listSchedules
);

router.post(
  '/resources',
  CorporateController.addResource
);

router.get(
  '/resources',
  CorporateController.listResources
);

router.put(
  '/resources/:id/reserve',
  CorporateController.reserveResource
);

module.exports = router;