package ejemplo;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class CalculatorClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12347;

    public static void main(String[] args) {
        System.out.println("Conectando al Servidor de Calculadora en " + SERVER_ADDRESS + ":" + SERVER_PORT);

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner consoleInput = new Scanner(System.in)) {

            // Leer los mensajes de bienvenida del servidor
            String serverMessage;
            while ((serverMessage = in.readLine()) != null) {
                System.out.println("Servidor: " + serverMessage);
                // Ahora la última línea de bienvenida es la de funciones trigonométricas
                if (serverMessage.contains("atan 1")) {
                    break;
                }
            }

            String userInput;
            while (true) {
                System.out.print("Introduce operación (ej: 5 + 3) o 'quit': ");
                userInput = consoleInput.nextLine();

                if ("quit".equalsIgnoreCase(userInput.trim())) {
                    out.println(userInput);
                    System.out.println("Servidor: " + in.readLine());
                    break;
                }

                out.println(userInput);
                String serverResponse = in.readLine();
                System.out.println("Servidor: " + serverResponse);
            }

        } catch (UnknownHostException e) {
            System.err.println("ERROR: Host desconocido: " + SERVER_ADDRESS);
        } catch (IOException e) {
            System.err.println("ERROR: No se pudo conectar al servidor de calculadora: " + e.getMessage());
        } finally {
            System.out.println("Cliente de Calculadora desconectado.");
        }
    }
}