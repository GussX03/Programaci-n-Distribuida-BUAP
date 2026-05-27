const express = require('express');
const cors = require('cors');

const app = express();

app.use(cors());
app.use(express.json());

app.post('/translate', async (req, res) => {
    try {
        const { text, targetLang } = req.body;

        // Simulación de traducción
        const translated = `[${targetLang}] ${text}`;

        res.json({ translated });

    } catch (error) {
        res.status(500).json({
            error: 'Error en traducción'
        });
    }
});

app.listen(4002, () => {
    console.log("Translation Service en puerto 4002");
});