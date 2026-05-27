import React,
{
  useState,
  useEffect
} from 'react';

export default function CorporateClient() {

  const [employees, setEmployees] =
    useState([]);

  const [resources, setResources] =
    useState([]);

  const [name, setName] =
    useState("");

  const [department, setDepartment] =
  useState("");

  const fetchEmployees = async () => {

    const res =
      await fetch(
        "http://localhost:10001/corporate/employees"
      );

    setEmployees(
      await res.json()
    );
  };

  const fetchResources = async () => {

    const res =
      await fetch(
        "http://localhost:10001/corporate/resources"
      );

    setResources(
      await res.json()
    );
  };

  const addEmployee = async () => {

    await fetch(
      "http://localhost:10001/corporate/employees",
      {

        method: "POST",

        headers: {
          "Content-Type": "application/json"
        },

        body: JSON.stringify({
          name, department
        })
      }
    );

    setName("");

    setDepartment("");

    fetchEmployees();
  };

  const reserveResource = async (id) => {

    await fetch(

      `http://localhost:10001/corporate/resources/${id}/reserve`,

      {
        method: "PUT"
      }
    );

    fetchResources();
  };

  useEffect(() => {

    fetchEmployees();

    fetchResources();

  }, []);

  return (

    <div>

      <h2>
        Gestión Corporativa Interna
      </h2>

      <h3>
        Empleados
      </h3>

      <input

        value={name}

        onChange={
          e => setName(e.target.value)
        }

        placeholder="Nombre empleado"
      />

      <input
        value={department}
        
        onChange={
          e => setDepartment(e.target.value)
        }

        placeholder="Departamento"
      />

      <button onClick={addEmployee}>
        Agregar
      </button>

      <ul>

        {employees.map(e => (

          <li key={e._id}>

            {e.name}
            {" - "}
            {e.department || "Sin depto"}

          </li>

        ))}

      </ul>

      <h3>
        Recursos
      </h3>

      <ul>

        {resources.map(r => (

          <li key={r._id}>

            {r.name}
            {" ("}
            {r.type}
            {") - "}

            {r.availability
              ? "Disponible"
              : "Reservado"}

            {r.availability && (

              <button
                onClick={() =>
                  reserveResource(r._id)
                }
              >
                Reservar
              </button>

            )}

          </li>

        ))}

      </ul>

    </div>
  );
}