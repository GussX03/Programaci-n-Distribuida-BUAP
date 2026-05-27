const express = require('express');
const cors = require('cors');

const app = express();

app.use(cors());
app.use(express.json());

function haversine(lat1, lon1, lat2, lon2) {

    const R = 6371;

    const dLat = (lat2 - lat1) * Math.PI / 180;
    const dLon = (lon2 - lon1) * Math.PI / 180;

    const a =
        Math.sin(dLat / 2) ** 2 +
        Math.cos(lat1 * Math.PI / 180) *
        Math.cos(lat2 * Math.PI / 180) *
        Math.sin(dLon / 2) ** 2;

    return 2 * R * Math.asin(Math.sqrt(a));
}

app.post('/distance', (req, res) => {

    try {

        const { lat1, lon1, lat2, lon2 } = req.body;

        const distance = haversine(
            lat1,
            lon1,
            lat2,
            lon2
        );

        res.json({
            distanceKm: distance.toFixed(2)
        });

    } catch (error) {

        res.status(500).json({
            error: 'Error calculando distancia'
        });
    }
});

app.listen(4004, () => {
    console.log("Geo Service en puerto 4004");
});