const express = require('express');

const cors = require('cors');

const app = express();

app.use(cors());

app.use(express.json());

function analyzeSentiment(text) {

  if (text.includes("bueno"))
    return "positivo";

  if (text.includes("malo"))
    return "negativo";

  return "neutro";
}

app.post('/sentiment', (req, res) => {

  const { text } = req.body;

  res.json({
    sentiment: analyzeSentiment(text)
  });
});

app.listen(4003, () =>

  console.log(
    "Sentiment Service en puerto 4003"
  )
);