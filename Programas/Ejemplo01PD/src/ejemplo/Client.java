package ejemplo;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        String host = "localhost"; // Dirección IP del servidor (en este caso, la misma máquina)
        int port = 1234; // Puerto del servidor
        try (Socket socket = new Socket(host, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Conectado al servidor en " + host + ":" + port);
            System.out.println("Escribe mensajes (escribe 'adios' para salir):");

            String userInput;
            while ((userInput = consoleInput.readLine())!= null) {
                out.println(userInput); // Envía el mensaje al servidor
                String serverResponse = in.readLine(); // Recibe la respuesta del servidor
                System.out.println("Respuesta del servidor: " + serverResponse);

                if (userInput.equals("adios")) {
                    break;
                }
            }
            System.out.println("Desconectado del servidor.");

        } catch (UnknownHostException e) {
            System.err.println("Host desconocido: " + host);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error de I/O al conectar al servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
