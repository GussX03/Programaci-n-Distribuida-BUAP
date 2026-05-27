import React,
{
  useState,
  useEffect
} from 'react';

export default function TaskClient() {

  const [tasks, setTasks] =
    useState([]);

  const [title, setTitle] =
    useState("");

  const fetchTasks = async () => {

    const res =
      await fetch(
        "http://localhost:6001/tasks"
      );

    setTasks(
      await res.json()
    );
  };

  const addTask = async () => {

    await fetch(
      "http://localhost:6001/tasks",
      {

        method: "POST",

        headers: {
          "Content-Type": "application/json"
        },

        body: JSON.stringify({
          title
        })
      }
    );

    setTitle("");

    fetchTasks();
  };

  const deleteTask = async (id) => {

    await fetch(

      `http://localhost:6001/tasks/${id}`,

      {
        method: "DELETE"
      }
    );

    fetchTasks();
  };

  useEffect(() => {

    fetchTasks();

  }, []);

  return (

    <div>

      <h2>
        Gestión de Tareas
      </h2>

      <input

        value={title}

        onChange={
          e => setTitle(e.target.value)
        }

        placeholder="Nueva tarea"
      />

      <button onClick={addTask}>
        Agregar
      </button>

      <ul>

        {tasks.map(t => (

          <li key={t._id}>

            {t.title}
            {" - "}
            {t.status}

            <button
              onClick={() =>
                deleteTask(t._id)
              }
            >
              Eliminar
            </button>

          </li>

        ))}

      </ul>

    </div>
  );
}