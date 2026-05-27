package ejemplo;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TaskClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 5001;

    public static void main(String[] args) {
        System.out.println("Conectando al Servidor de Tareas en " + SERVER_ADDRESS + ":" + SERVER_PORT);
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             Scanner consoleInput = new Scanner(System.in)) {

            // Lee el mensaje de bienvenida del servidor
            System.out.println(in.readObject());

            String userInput;
            while (true) {
                System.out.print("> "); // Prompt para el usuario
                userInput = consoleInput.nextLine();

                if (userInput.trim().isEmpty()) {
                    continue; // Ignora entradas vacías
                }

                out.writeObject(userInput); // Envía el comando al servidor

                if (userInput.equalsIgnoreCase("QUIT")) {
                    System.out.println("Desconectando...");
                    break;
                }

                // Lee y muestra la respuesta del servidor
                if (userInput.toUpperCase().startsWith("LIST")) {
                    String responseString;
                    while ((responseString = (String) in.readObject()) != null) {
                        System.out.println(responseString);
                        if (responseString.equals("--- Fin de lista ---") || responseString.equals("OK: No hay tareas.")) {
                            break;
                        }
                    }
                } else {
                    String responseString = (String) in.readObject();
                    System.out.println(responseString);
                }
            }

        } catch (UnknownHostException e) {
            System.err.println("ERROR: Host desconocido: " + SERVER_ADDRESS);
        } catch (EOFException e) {
            System.err.println("El servidor cerró la conexión inesperadamente.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error de comunicación con el servidor: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("Cliente desconectado.");
    }
}
