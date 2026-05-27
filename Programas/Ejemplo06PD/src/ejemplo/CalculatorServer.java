package ejemplo;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CalculatorServer {
    private static final int PORT = 12347;
    private static final int THREAD_POOL_SIZE = 5;

    public static void main(String[] args) {
        System.out.println("Servidor de Calculadora iniciado en el puerto " + PORT);
        ExecutorService pool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nuevo cliente conectado desde: " + clientSocket.getInetAddress().getHostAddress());
                pool.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("Error fatal del servidor: " + e.getMessage());
            e.printStackTrace();
        } finally {
            pool.shutdown();
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
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String inputLine;
            out.println("Bienvenido a la Calculadora TCP. Formato: numero1 operador numero2 (ej: 5 + 3)");
            out.println("Operadores soportados: +, -, *, /, %. Escribe 'quit' para salir.");
            out.println("Funciones trigonométricas (ángulo en grados): sin 45, cos 60, tan 30, atan 1");

            while ((inputLine = in.readLine()) != null) {
                System.out.println("Recibido de " + clientSocket.getInetAddress().getHostAddress() + ": " + inputLine);

                if ("quit".equalsIgnoreCase(inputLine.trim())) {
                    out.println("Adiós.");
                    break;
                }

                String result = calculate(inputLine);
                out.println(result);
            }

        } catch (IOException e) {
            System.err.println("Error en la comunicación con el cliente " + clientSocket.getInetAddress().getHostAddress() + ": " + e.getMessage());
        } finally {
            try {
                if (clientSocket != null && !clientSocket.isClosed()) {
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
            String[] parts = operation.split(" ");

            // Funciones trigonométricas (formato: funcion numero, ej: sin 45)
            if (parts.length == 2) {
                String func = parts[0].toLowerCase();
                double angleGrados = Double.parseDouble(parts[1]);
                double angleRadianes = Math.toRadians(angleGrados);

                switch (func) {
                    case "sin":  return "RESULTADO: " + Math.sin(angleRadianes);
                    case "cos":  return "RESULTADO: " + Math.cos(angleRadianes);
                    case "tan":
                        if (angleGrados % 180 == 90)
                            return "ERROR: tan(" + angleGrados + "°) no está definida.";
                        return "RESULTADO: " + Math.tan(angleRadianes);
                    case "atan": return "RESULTADO: " + Math.toDegrees(Math.atan(angleGrados)) + "°";
                    default:     return "ERROR: Función no reconocida. Soportadas: sin, cos, tan, atan";
                }
            }

            // Operaciones básicas (formato original)
            if (parts.length != 3) {
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