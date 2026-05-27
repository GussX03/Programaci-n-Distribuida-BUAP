const Employee =
  require('../models/Employee');

const Schedule =
  require('../models/Schedule');

const Resource =
  require('../models/Resource');

class CorporateService {

  async addEmployee(data) {

    const employee =
      new Employee(data);

    return await employee.save();
  }

  async assignSchedule(data) {

    const schedule =
      new Schedule(data);

    return await schedule.save();
  }

  async addResource(data) {

    const resource =
      new Resource(data);

    return await resource.save();
  }

  async reserveResource(id) {

    return await Resource.findByIdAndUpdate(

      id,

      {
        availability: false
      },

      {
        new: true
      }
    );
  }

  async listEmployees() {

    return await Employee.find();
  }

  async listSchedules() {

    return await Schedule.find()
      .populate('employeeId');
  }

  async listResources() {

    return await Resource.find();
  }
}

module.exports =
  new CorporateService();