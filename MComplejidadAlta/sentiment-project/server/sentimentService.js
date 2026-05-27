const express = require('express');
const cors = require('cors');

const app = express();

app.use(cors());
app.use(express.json());

function analyzeSentiment(text) {

    const lowerText = text.toLowerCase();

    if (
        lowerText.includes("bueno") ||
        lowerText.includes("excelente") ||
        lowerText.includes("genial")
    ) {
        return "positivo";
    }

    if (
        lowerText.includes("malo") ||
        lowerText.includes("terrible") ||
        lowerText.includes("horrible")
    ) {
        return "negativo";
    }

    return "neutro";
}

app.post('/sentiment', (req, res) => {

    try {

        const { text } = req.body;

        const sentiment =
            analyzeSentiment(text);

        res.json({
            sentiment
        });

    } catch (error) {

        res.status(500).json({
            error: 'Error analizando sentimiento'
        });
    }
});

app.listen(4003, () => {

    console.log(
        "Sentiment Service en puerto 4003"
    );
});