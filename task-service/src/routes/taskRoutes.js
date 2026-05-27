const express = require('express');

const TaskController =
  require('../controllers/TaskController');

const router = express.Router();

router.post(
  '/',
  TaskController.create
);

router.get(
  '/',
  TaskController.list
);

router.put(
  '/:id',
  TaskController.update
);

router.delete(
  '/:id',
  TaskController.delete
);

module.exports = router;