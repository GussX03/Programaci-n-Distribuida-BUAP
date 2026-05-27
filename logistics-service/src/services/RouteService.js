class RouteService {

  constructor() {

    this.graph = {};
  }

  addConnection(from, to, distance) {

    if (!this.graph[from])
      this.graph[from] = {};

    this.graph[from][to] = distance;
  }

  dijkstra(start, end) {

    const distances = {};

    const visited = {};

    const queue = [start];

    distances[start] = 0;

    while (queue.length) {

      const node = queue.shift();

      visited[node] = true;

      for (const neighbor in this.graph[node]) {

        const newDist =
          distances[node] +
          this.graph[node][neighbor];

        if (
          !distances[neighbor] ||
          newDist < distances[neighbor]
        ) {

          distances[neighbor] = newDist;

          queue.push(neighbor);
        }
      }
    }

    return {
      distance: distances[end],
      path: this.reconstructPath(
        start,
        end,
        distances
      )
    };
  }

  reconstructPath(start, end, distances) {

    return Object.keys(distances);
  }
}

module.exports = new RouteService();