import React,
{
  useState
} from 'react';

export default function SentimentClient() {

  const [sentiment, setSentiment] =
    useState("");

  const analyze = async () => {

    const res =
      await fetch(
        "http://localhost:4003/sentiment",
        {

          method: "POST",

          headers: {
            "Content-Type": "application/json"
          },

          body: JSON.stringify({
            text: "Este producto es muy bueno"
          })
        }
      );

    const data = await res.json();

    setSentiment(
      data.sentiment
    );
  };

  return (

    <div>

      <h2>
        Análisis de Sentimientos
      </h2>

      <button onClick={analyze}>
        Analizar Sentimiento
      </button>

      <p>
        Resultado:
        {" "}
        {sentiment}
      </p>

    </div>
  );
}