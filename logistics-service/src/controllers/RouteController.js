const RouteService =
  require('../services/RouteService');

class RouteController {

  async addConnection(req, res) {

    const { from, to, distance } =
      req.body;

    RouteService.addConnection(
      from,
      to,
      distance
    );

    res.json({
      message: "Conexión agregada"
    });
  }

  async calculateRoute(req, res) {

    const { start, end } =
      req.body;

    const result =
      RouteService.dijkstra(start, end);

    res.json(result);
  }
}

module.exports =
  new RouteController();