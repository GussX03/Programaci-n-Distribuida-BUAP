import React,
{
  useState
} from 'react';

export default function RecommendationClient() {

  const [
    recommendations,
    setRecommendations
  ] = useState([]);

  const fetchRecommendations =
    async () => {

      const res =
        await fetch(
          "http://localhost:4001/recommend",
          {

            method: "POST",

            headers: {
              "Content-Type": "application/json"
            },

            body: JSON.stringify({
              userId: 1
            })
          }
        );

      setRecommendations(
        await res.json()
      );
    };

  return (

    <div>

      <h2>
        Sistema de Recomendaciones
      </h2>

      <button
        onClick={fetchRecommendations}
      >
        Obtener recomendaciones
      </button>

      <ul>

        {recommendations.map(r => (

          <li key={r.item}>

            {r.item}
            {": "}
            {r.score}

          </li>

        ))}

      </ul>

    </div>
  );
}