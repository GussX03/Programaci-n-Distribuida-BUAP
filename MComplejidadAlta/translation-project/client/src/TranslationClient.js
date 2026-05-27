import React, { useState } from 'react';

export default function TranslationClient() {

    const [text, setText] = useState("Hola mundo");
    const [targetLang, setTargetLang] = useState("en");
    const [result, setResult] = useState("");

    const translate = async () => {

        try {

            const res = await fetch("http://localhost:4002/translate", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    text,
                    targetLang
                })
            });

            const data = await res.json();

            setResult(data.translated);

        } catch (error) {
            console.error(error);
        }
    };

    return (
        <div style={{
            padding: '30px',
            fontFamily: 'Arial'
        }}>

            <h2>Servicio de Traducción</h2>

            <input
                type="text"
                value={text}
                onChange={(e) => setText(e.target.value)}
                placeholder="Texto"
                style={{
                    padding: '8px',
                    width: '250px',
                    marginBottom: '10px'
                }}
            />

            <br />

            <select
                value={targetLang}
                onChange={(e) => setTargetLang(e.target.value)}
                style={{
                    padding: '8px',
                    marginBottom: '10px'
                }}
            >
                <option value="en">Inglés</option>
                <option value="fr">Francés</option>
                <option value="it">Italiano</option>
                <option value="de">Alemán</option>
            </select>

            <br />

            <button
                onClick={translate}
                style={{
                    padding: '10px 20px',
                    cursor: 'pointer'
                }}
            >
                Traducir
            </button>

            <p>
                Resultado: {result}
            </p>

        </div>
    );
}