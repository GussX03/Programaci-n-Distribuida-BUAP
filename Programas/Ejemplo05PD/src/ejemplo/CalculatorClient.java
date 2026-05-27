package ejemplo;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class CalculatorClient {
    private static final String SERVER_ADDRESS = "localhost"; // O la IP del servidor si no está en la misma máquina
    private static final int SERVER_PORT = 12346;

    public static void main(String[] args) {
        System.out.println("Conectando al Servidor de Calculadora en " + SERVER_ADDRESS + ":" + SERVER_PORT);

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true); // 'true' para auto-flush
             Scanner consoleInput = new Scanner(System.in)) {

            // Leer los mensajes de bienvenida del servidor
            String serverMessage;
            while ((serverMessage = in.readLine())!= null) {
                System.out.println("Servidor: " + serverMessage);
                if (serverMessage.contains("Escribe 'quit' para salir.")) {
                    break; // Hemos leído todos los mensajes de bienvenida
                }
            }

            String userInput;
            while (true) {
                System.out.print("Introduce operación (ej: 5 + 3) o 'quit': ");
                userInput = consoleInput.nextLine();

                if ("quit".equalsIgnoreCase(userInput.trim())) {
                    out.println(userInput); // Envía "quit" al servidor
                    System.out.println("Servidor: " + in.readLine()); // Espera la respuesta de "Adiós."
                    break;
                }

                out.println(userInput); // Envía la operación al servidor
                String serverResponse = in.readLine(); // Espera y lee la respuesta del servidor
                System.out.println("Servidor: " + serverResponse);
            }

        } catch (UnknownHostException e) {
            System.err.println("ERROR: Host desconocido: " + SERVER_ADDRESS);
        } catch (IOException e) {
            System.err.println("ERROR: No se pudo conectar al servidor de calculadora: " + e.getMessage());
            // e.printStackTrace(); // Descomentar para depuración
        } finally {
            System.out.println("Cliente de Calculadora desconectado.");
        }
    }
}
