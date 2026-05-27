import React, { useState } from 'react';

import {
  MapContainer,
  TileLayer,
  Polyline
} from 'react-leaflet';

import 'leaflet/dist/leaflet.css';

export default function LogisticsClient() {

  const [path, setPath] =
    useState([]);

  const calculateRoute = async () => {

    await fetch(
      "http://localhost:9001/routes/connection",
      {
        method: "POST",

        headers: {
          "Content-Type": "application/json"
        },

        body: JSON.stringify({
          from: "A",
          to: "B",
          distance: 5
        })
      }
    );

    await fetch(
      "http://localhost:9001/routes/connection",
      {
        method: "POST",

        headers: {
          "Content-Type": "application/json"
        },

        body: JSON.stringify({
          from: "B",
          to: "C",
          distance: 7
        })
      }
    );

    const res =
      await fetch(
        "http://localhost:9001/routes/calculate",
        {
          method: "POST",

          headers: {
            "Content-Type": "application/json"
          },

          body: JSON.stringify({
            start: "A",
            end: "C"
          })
        }
      );

    const data = await res.json();

    setPath(data.path);
  };

  return (

    <div>

      <h2>
        Optimización de Rutas
      </h2>

      <button onClick={calculateRoute}>
        Calcular Ruta
      </button>

      <MapContainer
        center={[19.4326, -99.1332]}
        zoom={5}
        style={{
          height: "400px",
          width: "600px"
        }}
      >

        <TileLayer
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />

        {path.length > 0 && (

          <Polyline
            positions={[
              [19.4326, -99.1332],
              [40.7128, -74.0060]
            ]}
            color="blue"
          />

        )}

      </MapContainer>

      <p>
        Ruta calculada:
        {path.join(" → ")}
      </p>

    </div>
  );
}