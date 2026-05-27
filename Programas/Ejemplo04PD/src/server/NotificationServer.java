package server;

import server.service.NotificationService;
import server.session.ClientSessionHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotificationServer {
    private static final int PORT = 5002;
    private static final int MAX_THREADS = 10; // Pool de hilos para manejar clientes

    public static void main(String[] args) {
        System.out.println("Iniciando Servidor de Notificaciones en el puerto " + PORT + "...");
        NotificationService notificationService = new NotificationService(); // Servicio central de lógica
        ExecutorService pool = Executors.newFixedThreadPool(MAX_THREADS); // Pool de hilos

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor iniciado, esperando conexiones...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nuevo cliente conectado desde: " + clientSocket.getInetAddress().getHostAddress());
                // Cada cliente se maneja en un hilo separado del pool
                pool.execute(new ClientSessionHandler(clientSocket, notificationService));
            }
        } catch (IOException e) {
            System.err.println("Error fatal del servidor: " + e.getMessage());
            e.printStackTrace();
        } finally {
            pool.shutdown(); // Apaga el pool de hilos de forma ordenada
            System.out.println("Servidor de Notificaciones apagado.");
        }
    }
}
