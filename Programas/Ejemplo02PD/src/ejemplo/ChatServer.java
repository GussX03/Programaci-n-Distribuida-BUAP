package ejemplo;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 1234;
    private static Set<PrintWriter> clientWriters = new HashSet<>(); // Para enviar a todos los clientes

    public static void main(String[] args) {
        System.out.println("Servidor de Chat iniciado en el puerto " + PORT);
        try (ServerSocket listener = new ServerSocket(PORT)) {
            while (true) {
                // Acepta una nueva conexión de cliente y la pasa a un nuevo hilo
                new ClientHandler(listener.accept()).start();
            }
        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }

    // Clase interna para manejar cada cliente en su propio hilo
    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private String clientName;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                // Configura los streams de entrada y salida
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Pide al cliente su nombre y lo guarda
                out.println("SUBMITNAME"); // Le dice al cliente que envíe su nombre
                clientName = in.readLine();
                if (clientName == null) {
                    return;
                }
                synchronized (clientWriters) {
                    clientWriters.add(out); // Agrega este cliente a la lista de emisores
                }

                System.out.println(clientName + " se ha unido al chat.");
                broadcastMessage("MESSAGE " + clientName + " se ha unido al chat.");

                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println(clientName + ": " + message);
                    if (message.toLowerCase().equals("adios")) {
                        break;
                    }
                    broadcastMessage("MESSAGE " + clientName + ": " + message);
                }
            } catch (IOException e) {
                System.err.println("Error de I/O para el cliente " + clientName + ": " + e.getMessage());
            } finally {
                if (clientName != null) {
                    System.out.println(clientName + " ha salido del chat.");
                    broadcastMessage("MESSAGE " + clientName + " ha salido del chat.");
                }
                if (out != null) {
                    synchronized (clientWriters) {
                        clientWriters.remove(out);
                    }
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    System.err.println("Error al cerrar el socket del cliente: " + e.getMessage());
                }
            }
        }

        // Método para enviar un mensaje a todos los clientes conectados
        private void broadcastMessage(String message) {
            synchronized (clientWriters) {
                for (PrintWriter writer : clientWriters) {
                    writer.println(message);
                }
            }
        }
    }
}

