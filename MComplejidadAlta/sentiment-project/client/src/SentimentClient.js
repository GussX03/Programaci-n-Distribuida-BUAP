import React, { useState } from 'react';

export default function SentimentClient() {

    const [text, setText] = useState("");
    const [sentiment, setSentiment] =
        useState("");

    const analyze = async () => {

        try {

            const res = await fetch(
                "http://localhost:4003/sentiment",
                {
                    method: "POST",

                    headers: {
                        "Content-Type": "application/json"
                    },

                    body: JSON.stringify({
                        text
                    })
                }
            );

            const data = await res.json();

            setSentiment(data.sentiment);

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
                Análisis de Sentimientos
            </h2>

            <textarea
                rows="4"
                cols="40"
                placeholder="Escribe un texto..."
                value={text}
                onChange={(e) =>
                    setText(e.target.value)
                }
                style={{
                    padding: '10px'
                }}
            />

            <br /><br />

            <button
                onClick={analyze}
                style={{
                    padding: '10px 20px'
                }}
            >
                Analizar Sentimiento
            </button>

            <p>

                Resultado:
                <strong>
                    {" "}{sentiment}
                </strong>

            </p>

        </div>
    );
}