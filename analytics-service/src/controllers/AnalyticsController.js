const AnalyticsService =
  require('../services/AnalyticsService');

class AnalyticsController {

  async addSale(req, res) {

    try {

      const sale =
        await AnalyticsService.addSale(req.body);

      res.status(201).json(sale);

    } catch (err) {

      res.status(400).json({
        error: err.message
      });

    }
  }

  async listSales(req, res) {

    const sales =
      await AnalyticsService.getSales();

    res.json(sales);
  }

  async monthlyStats(req, res) {

    const stats =
      await AnalyticsService.getMonthlyStats();

    res.json(stats);
  }

  async topProducts(req, res) {

    const products =
      await AnalyticsService.getTopProducts();

    res.json(products);
  }
}

module.exports =
  new AnalyticsController();