package ejemplo;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CalculatorServer {
    private static final int PORT = 12346; // Un puerto diferente al de otros ejemplos
    private static final int THREAD_POOL_SIZE = 5; // Número de clientes que puede manejar simultáneamente

    public static void main(String[] args) {
        System.out.println("Servidor de Calculadora iniciado en el puerto " + PORT);
        // Creamos un pool de hilos para manejar a los clientes de forma concurrente.
        ExecutorService pool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                // Espera por una conexión de cliente
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nuevo cliente conectado desde: " + clientSocket.getInetAddress().getHostAddress());
                // Asigna la conexión a un hilo del pool para que la maneje
                pool.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("Error fatal del servidor: " + e.getMessage());
            e.printStackTrace();
        } finally {
            pool.shutdown(); // Apaga el pool de hilos de forma ordenada
            System.out.println("Servidor de Calculadora apagado.");
        }
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) { // 'true' para auto-flush

            String inputLine;
            // El servidor espera recibir operaciones del cliente
            out.println("Bienvenido a la Calculadora TCP. Formato: numero1 operador numero2 (ej: 5 + 3)");
            out.println("Operadores soportados: +, -, *, /, %. Escribe 'quit' para salir.");

            while ((inputLine = in.readLine())!= null) {
                System.out.println("Recibido de " + clientSocket.getInetAddress().getHostAddress() + ": " + inputLine);

                if ("quit".equalsIgnoreCase(inputLine.trim())) {
                    out.println("Adiós.");
                    break;
                }

                String result = calculate(inputLine);
                out.println(result); // Envía el resultado (o error) de vuelta al cliente
            }

        } catch (IOException e) {
            System.err.println("Error en la comunicación con el cliente " + clientSocket.getInetAddress().getHostAddress() + ": " + e.getMessage());
        } finally {
            try {
                if (clientSocket!= null &&!clientSocket.isClosed()) {
                    clientSocket.close();
                    System.out.println("Cliente " + clientSocket.getInetAddress().getHostAddress() + " desconectado.");
                }
            } catch (IOException e) {
                System.err.println("Error al cerrar el socket del cliente: " + e.getMessage());
            }
        }
    }

    private String calculate(String operation) {
        try {
            // Divide la cadena de operación (ej: "5 + 3") en partes
            String[] parts = operation.split(" ");
            if (parts.length!= 3) {
                return "ERROR: Formato incorrecto. Uso: numero1 operador numero2";
            }

            double num1 = Double.parseDouble(parts[0]);
            String operator = parts[1];
            double num2 = Double.parseDouble(parts[2]);

            double result;
            switch (operator) {
                case "+":
                    result = num1 + num2;
                    break;
                case "-":
                    result = num1 - num2;
                    break;
                case "*":
                    result = num1 * num2;
                    break;
                case "/":
                    if (num2 == 0) {
                        return "ERROR: División por cero no permitida.";
                    }
                    result = num1 / num2;
                    break;
                case "%":
                    result = num1 % num2;
                    break;
                default:
                    return "ERROR: Operador no válido. Soportados: +, -, *, /, %";
            }
            return "RESULTADO: " + result;

        } catch (NumberFormatException e) {
            return "ERROR: Números inválidos proporcionados.";
        } catch (Exception e) {
            return "ERROR: Ha ocurrido un error inesperado: " + e.getMessage();
        }
    }
}
