const CorporateService =
  require('../services/CorporateService');

class CorporateController {

  async addEmployee(req, res) {

    const employee =
      await CorporateService.addEmployee(
        req.body
      );

    res.status(201).json(employee);
  }

  async assignSchedule(req, res) {

    const schedule =
      await CorporateService.assignSchedule(
        req.body
      );

    res.status(201).json(schedule);
  }

  async addResource(req, res) {

    const resource =
      await CorporateService.addResource(
        req.body
      );

    res.status(201).json(resource);
  }

  async reserveResource(req, res) {

    const resource =
      await CorporateService.reserveResource(
        req.params.id
      );

    res.json(resource);
  }

  async listEmployees(req, res) {

    res.json(
      await CorporateService.listEmployees()
    );
  }

  async listSchedules(req, res) {

    res.json(
      await CorporateService.listSchedules()
    );
  }

  async listResources(req, res) {

    res.json(
      await CorporateService.listResources()
    );
  }
}

module.exports =
  new CorporateController();