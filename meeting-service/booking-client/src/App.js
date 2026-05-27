import React, { useState, useEffect } from 'react';

export default function BookingClient() {

  const [bookings, setBookings] = useState([]);

  const [room, setRoom] = useState("");

  const [user, setUser] = useState("");

  const [startTime, setStartTime] = useState("");

  const [endTime, setEndTime] = useState("");

  const fetchBookings = async () => {

    const res =
      await fetch("http://localhost:7001/bookings");

    setBookings(await res.json());
  };

  const addBooking = async () => {

    await fetch(
      "http://localhost:7001/bookings",
      {
        method: "POST",

        headers: {
          "Content-Type": "application/json"
        },

        body: JSON.stringify({
          room,
          user,
          startTime,
          endTime
        })
      }
    );

    fetchBookings();
  };

  const cancelBooking = async (id) => {

    await fetch(
      `http://localhost:7001/bookings/${id}/cancel`,
      {
        method: "PUT"
      }
    );

    fetchBookings();
  };

  useEffect(() => {

    fetchBookings();

  }, []);

  return (

    <div>

      <h2>Reservas de Salas</h2>

      <input
        value={room}
        onChange={e => setRoom(e.target.value)}
        placeholder="Sala"
      />

      <input
        value={user}
        onChange={e => setUser(e.target.value)}
        placeholder="Usuario"
      />

      <input
        type="datetime-local"
        value={startTime}
        onChange={e => setStartTime(e.target.value)}
      />

      <input
        type="datetime-local"
        value={endTime}
        onChange={e => setEndTime(e.target.value)}
      />

      <button onClick={addBooking}>
        Reservar
      </button>

      <ul>

        {bookings.map(b => (

          <li key={b._id}>

            {b.room} - {b.user}

            ({b.startTime} a {b.endTime})

            [{b.status}]

            {b.status === 'reserved' && (

              <button
                onClick={() =>
                  cancelBooking(b._id)
                }
              >
                Cancelar
              </button>

            )}

          </li>

        ))}

      </ul>

    </div>
  );
}