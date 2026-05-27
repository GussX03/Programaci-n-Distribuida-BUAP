package ejemplo;

import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        int port = 1234; // Puerto en el que el servidor escuchará
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor iniciado en el puerto " + port);
            System.out.println("Esperando conexiones de clientes...");

            // El servidor espera a que un cliente se conecte
            Socket clientSocket = serverSocket.accept();
            System.out.println("Cliente conectado desde: " + clientSocket.getInetAddress().getHostAddress());

            // Configurar streams de entrada y salida para la comunicación con el cliente
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true); // 'true' para auto-flush

            String inputLine;
            while ((inputLine = in.readLine())!= null) {
                System.out.println("Mensaje del cliente: " + inputLine);
                out.println("Eco del servidor: " + inputLine); // El servidor responde con un eco
                if (inputLine.equals("adios")) {
                    break;
                }
            }

            System.out.println("Cliente desconectado.");
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

