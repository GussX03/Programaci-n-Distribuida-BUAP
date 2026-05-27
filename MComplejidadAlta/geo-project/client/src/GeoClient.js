import React, { useState } from 'react';

export default function GeoClient() {

    const [lat1, setLat1] = useState("19.4326");
    const [lon1, setLon1] = useState("-99.1332");

    const [lat2, setLat2] = useState("40.7128");
    const [lon2, setLon2] = useState("-74.0060");

    const [distance, setDistance] = useState("");

    const calculate = async () => {

        try {

            const res = await fetch(
                "http://localhost:4004/distance",
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        lat1: parseFloat(lat1),
                        lon1: parseFloat(lon1),
                        lat2: parseFloat(lat2),
                        lon2: parseFloat(lon2)
                    })
                }
            );

            const data = await res.json();

            setDistance(data.distanceKm);

        } catch (error) {
            console.error(error);
        }
    };

    return (
        <div style={{
            padding: '30px',
            fontFamily: 'Arial'
        }}>

            <h2>Microservicio de Geolocalización</h2>

            <h4>Punto 1</h4>

            <input
                type="text"
                placeholder="Latitud"
                value={lat1}
                onChange={(e) => setLat1(e.target.value)}
            />

            <input
                type="text"
                placeholder="Longitud"
                value={lon1}
                onChange={(e) => setLon1(e.target.value)}
            />

            <h4>Punto 2</h4>

            <input
                type="text"
                placeholder="Latitud"
                value={lat2}
                onChange={(e) => setLat2(e.target.value)}
            />

            <input
                type="text"
                placeholder="Longitud"
                value={lon2}
                onChange={(e) => setLon2(e.target.value)}
            />

            <br /><br />

            <button onClick={calculate}>
                Calcular distancia
            </button>

            <p>
                Distancia: {distance} km
            </p>

        </div>
    );
}