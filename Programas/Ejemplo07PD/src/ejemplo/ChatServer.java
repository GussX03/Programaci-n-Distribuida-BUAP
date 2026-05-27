package ejemplo;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 12345;
    private static final int MAX_MESSAGES = 10;
    private static Set<PrintWriter> clientWriters = new HashSet<>();
    private static List<String> pizarra = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Servidor de Pizarra iniciado en el puerto " + PORT);
        try (ServerSocket listener = new ServerSocket(PORT)) {
            while (true) {
                new ClientHandler(listener.accept()).start();
            }
        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private String clientName;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                out.println("SUBMITNAME");
                clientName = in.readLine();
                if (clientName == null) {
                    return;
                }

                synchronized (clientWriters) {
                    clientWriters.add(out);
                }

                // Al conectarse, envía el estado actual de la pizarra al nuevo cliente
                synchronized (pizarra) {
                    out.println("MESSAGE --- Pizarra actual (" + pizarra.size() + "/" + MAX_MESSAGES + " mensajes) ---");
                    for (String msg : pizarra) {
                        out.println("MESSAGE " + msg);
                    }
                    out.println("MESSAGE ---------------------------------------------------");
                }

                System.out.println(clientName + " se ha unido.");
                broadcastMessage("MESSAGE " + clientName + " se ha unido a la pizarra.");

                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println(clientName + ": " + message);
                    if (message.toLowerCase().equals("adios")) {
                        break;
                    }

                    String entrada = clientName + ": " + message;

                    synchronized (pizarra) {
                        pizarra.add(entrada);
                        broadcastMessage("MESSAGE " + entrada);

                        if (pizarra.size() >= MAX_MESSAGES) {
                            broadcastMessage("MESSAGE *** Pizarra llena (10 mensajes). Borrando... ***");
                            pizarra.clear();
                            broadcastMessage("MESSAGE *** Pizarra borrada. Puedes seguir escribiendo. ***");
                        }
                    }
                }

            } catch (IOException e) {
                System.err.println("Error de I/O para " + clientName + ": " + e.getMessage());
            } finally {
                if (clientName != null) {
                    System.out.println(clientName + " ha salido.");
                    broadcastMessage("MESSAGE " + clientName + " ha salido de la pizarra.");
                }
                if (out != null) {
                    synchronized (clientWriters) {
                        clientWriters.remove(out);
                    }
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    System.err.println("Error al cerrar socket: " + e.getMessage());
                }
            }
        }

        private void broadcastMessage(String message) {
            synchronized (clientWriters) {
                for (PrintWriter writer : clientWriters) {
                    writer.println(message);
                }
            }
        }
    }
}