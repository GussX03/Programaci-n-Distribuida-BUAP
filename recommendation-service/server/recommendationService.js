const express = require('express');

const cors = require('cors');

const bodyParser = require('body-parser');

const {
  cosineSimilarity
} = require('./utils');

const app = express();

app.use(cors());

app.use(bodyParser.json());

const userProfiles = {

  1: [0.2, 0.8, 0.5],

  2: [0.9, 0.1, 0.3]
};

const items = {

  A: [0.3, 0.7, 0.4],

  B: [0.8, 0.2, 0.1],

  C: [0.5, 0.5, 0.5]
};

app.post('/recommend', (req, res) => {

  const { userId } = req.body;

  const profile =
    userProfiles[userId];

  if (!profile) {

    return res
      .status(404)
      .send("Usuario no encontrado");
  }

  const scores =
    Object.entries(items).map(

      ([item, vector]) => ({

        item,

        score: cosineSimilarity(
          profile,
          vector
        )
      })
    );

  res.json(

    scores.sort(
      (a, b) => b.score - a.score
    )
  );
});

app.listen(4001, () =>

  console.log(
    "Recommendation Service en puerto 4001"
  )
);