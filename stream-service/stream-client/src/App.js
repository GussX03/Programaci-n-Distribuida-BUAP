import React,
{
  useEffect,
  useState
} from 'react';

import { io }
from 'socket.io-client';

const socket =
  io("http://localhost:4005");

export default function StreamClient() {

  const [temperature,
    setTemperature] =
      useState("");

  useEffect(() => {

    socket.on('data', (data) => {

      setTemperature(
        data.temperature
      );
    });

  }, []);

  return (

    <div>

      <h2>
        Streaming en Tiempo Real
      </h2>

      <h3>
        Temperatura actual:
      </h3>

      <p>

        {temperature}
        {" °C"}

      </p>

    </div>
  );
}