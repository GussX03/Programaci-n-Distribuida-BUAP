import React, {
    useEffect,
    useState
} from 'react';

import { io } from 'socket.io-client';

const socket =
    io('http://localhost:4005');

export default function StreamClient() {

    const [temperature, setTemperature] =
        useState("");

    useEffect(() => {

        socket.on('data', (data) => {

            setTemperature(
                data.temperature
            );
        });

        return () => {

            socket.off('data');
        };

    }, []);

    return (

        <div style={{
            padding: '30px',
            fontFamily: 'Arial'
        }}>

            <h2>
                Streaming en Tiempo Real
            </h2>

            <div style={{
                marginTop: '20px',
                fontSize: '30px'
            }}>

                🌡️ {temperature} °C

            </div>

        </div>
    );
}