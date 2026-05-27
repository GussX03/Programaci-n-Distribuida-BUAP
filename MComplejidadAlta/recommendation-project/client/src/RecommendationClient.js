import React, { useState } from 'react';

export default function RecommendationClient() {

    const [userId, setUserId] = useState(1);

    const [recommendations, setRecommendations] =
        useState([]);

    const fetchRecommendations = async () => {

        try {

            const res = await fetch(
                "http://localhost:4001/recommend",
                {
                    method: "POST",

                    headers: {
                        "Content-Type": "application/json"
                    },

                    body: JSON.stringify({
                        userId: Number(userId)
                    })
                }
            );

            const data = await res.json();

            setRecommendations(data);

        } catch (error) {

            console.error(error);
        }
    };

    return (
        <div style={{
            padding: '30px',
            fontFamily: 'Arial'
        }}>

            <h2>
                Sistema de Recomendaciones
            </h2>

            <input
                type="number"
                value={userId}
                onChange={(e) =>
                    setUserId(e.target.value)
                }
                placeholder="ID Usuario"
                style={{
                    padding: '8px',
                    marginRight: '10px'
                }}
            />

            <button
                onClick={fetchRecommendations}
                style={{
                    padding: '8px 15px'
                }}
            >
                Obtener recomendaciones
            </button>

            <ul>

                {recommendations.map((r) => (

                    <li key={r.item}>

                        Item {r.item}
                        → Similitud: {r.score}

                    </li>

                ))}

            </ul>

        </div>
    );
}