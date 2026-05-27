const Sale = require('../models/Sale');

class AnalyticsService {

  async addSale(data) {

    const sale = new Sale(data);

    return await sale.save();
  }

  async getSales() {

    return await Sale.find();
  }

  async getMonthlyStats() {

    const sales = await Sale.aggregate([

      {
        $group: {
          _id: { $month: "$date" },
          total: { $sum: "$amount" },
          count: { $sum: 1 }
        }
      },

      {
        $sort: { _id: 1 }
      }

    ]);

    return sales.map(s => ({
      month: s._id,
      total: s.total,
      average: s.total / s.count
    }));
  }

  async getTopProducts() {

    return await Sale.aggregate([

      {
        $group: {
          _id: "$product",
          total: { $sum: "$amount" }
        }
      },

      {
        $sort: { total: -1 }
      },

      {
        $limit: 5
      }

    ]);
  }
}

module.exports = new AnalyticsService();