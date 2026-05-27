const TaskService =
  require('../services/TaskService');

class TaskController {

  async create(req, res) {

    try {

      const task =
        await TaskService.createTask(
          req.body
        );

      res.status(201).json(task);

    } catch (err) {

      res.status(400).json({
        error: err.message
      });
    }
  }

  async list(req, res) {

    const tasks =
      await TaskService.getTasks();

    res.json(tasks);
  }

  async update(req, res) {

    try {

      const task =
        await TaskService.updateTask(
          req.params.id,
          req.body
        );

      res.json(task);

    } catch (err) {

      res.status(400).json({
        error: err.message
      });
    }
  }

  async delete(req, res) {

    await TaskService.deleteTask(
      req.params.id
    );

    res.status(204).send();
  }
}

module.exports =
  new TaskController();