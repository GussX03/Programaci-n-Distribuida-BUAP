package ejemplo;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger; // Para IDs de tareas únicos y seguros para hilos

public class TaskServer {
    private static final int PORT = 5001;
    private static List<Task> tasks = Collections.synchronizedList(new ArrayList<>()); // Lista de tareas sincronizada
    private static AtomicInteger taskIdCounter = new AtomicInteger(0); // Contador de ID de tareas

    public static void main(String[] args) {
        System.out.println("Servidor de Tareas iniciado en el puerto " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                // Acepta nuevas conexiones de clientes
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nuevo cliente conectado: " + clientSocket.getInetAddress().getHostAddress());
                new ClientHandler(clientSocket, tasks, taskIdCounter).start();
            }
        } catch (IOException e) {
            System.err.println("Error en el servidor de tareas: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

// Clase interna o separada para manejar cada cliente
class ClientHandler extends Thread {
    private Socket clientSocket;
    private List<Task> tasks;
    private AtomicInteger taskIdCounter;
    private ObjectOutputStream out; // Para enviar objetos y cadenas
    private ObjectInputStream in; // Para recibir objetos y cadenas

    public ClientHandler(Socket socket, List<Task> tasks, AtomicInteger taskIdCounter) {
        this.clientSocket = socket;
        this.tasks = tasks;
        this.taskIdCounter = taskIdCounter;
    }

    public void run() {
        try {
            // Se usa ObjectInputStream/OutputStream para poder enviar objetos serializables
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

            out.writeObject("Bienvenido al Servidor de Tareas. Escribe 'HELP' para ver los comandos.");

            String clientCommand;
            while ((clientCommand = (String) in.readObject())!= null) {
                System.out.println("Comando recibido de " + clientSocket.getInetAddress().getHostAddress() + ": " + clientCommand);
                if (clientCommand.equalsIgnoreCase("QUIT")) {
                    break;
                }
                processCommand(clientCommand);
            }
        } catch (EOFException e) {
            System.out.println("Cliente " + clientSocket.getInetAddress().getHostAddress() + " ha cerrado la conexión.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error en el manejador de cliente: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (clientSocket!= null) clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error al cerrar el socket del cliente: " + e.getMessage());
            }
            System.out.println("Cliente " + clientSocket.getInetAddress().getHostAddress() + " desconectado.");
        }
    }

    private void processCommand(String command) throws IOException {
        String[] parts = command.split(" ", 2); // Divide el comando en verbo y argumento
        String action = parts[0].toUpperCase();
        String argument = parts.length > 1? parts[1] : "";

        try {
            switch (action) {
                case "ADD":
                    if (!argument.isEmpty()) {
                        int newId = taskIdCounter.incrementAndGet();
                        Task newTask = new Task(newId, argument);
                        tasks.add(newTask);
                        out.writeObject("OK: Tarea añadida: " + newTask);
                    } else {
                        out.writeObject("ERROR: Formato incorrecto. Uso: ADD <descripción>");
                    }
                    break;
                case "LIST":
                    if (tasks.isEmpty()) {
                        out.writeObject("OK: No hay tareas.");
                    } else {
                        out.writeObject("OK: Lista de Tareas:");
                        for (Task t : tasks) {
                            out.writeObject(t.toString());
                        }
                        out.writeObject("--- Fin de lista ---"); // Marcador para el cliente
                    }
                    break;
                case "COMPLETE":
                    try {
                        int taskId = Integer.parseInt(argument);
                        boolean found = false;
                        for (Task t : tasks) {
                            if (t.getId() == taskId) {
                                t.setCompleted(true);
                                out.writeObject("OK: Tarea " + taskId + " marcada como completada.");
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            out.writeObject("ERROR: Tarea con ID " + taskId + " no encontrada.");
                        }
                    } catch (NumberFormatException e) {
                        out.writeObject("ERROR: Formato de ID incorrecto. Uso: COMPLETE <ID>");
                    }
                    break;
                case "HELP":
                    out.writeObject("OK: Comandos disponibles: ADD <descripción>, LIST, COMPLETE <ID>, QUIT");
                    break;
                default:
                    out.writeObject("ERROR: Comando desconocido. Escribe 'HELP' para ver los comandos.");
            }
        } catch (IOException e) {
            System.err.println("Error al escribir en el socket del cliente: " + e.getMessage());
            throw e; // Relanza para que se maneje en el bloque finally y cierre el socket
        }
    }
}

