package server.session;

import common.models.Notification;
import common.models.User;
import common.protocol.Request;
import common.protocol.Response;
import common.protocol.ResponseType;
import server.service.NotificationService;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientSessionHandler extends Thread {
    private Socket clientSocket;
    private NotificationService notificationService;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private User currentUser = null; // Usuario autenticado para esta sesión

    public ClientSessionHandler(Socket socket, NotificationService service) {
        this.clientSocket = socket;
        this.notificationService = service;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    // Método para enviar una notificación PUSH al cliente (desde el servicio)
    public void sendNotification(Notification notification) {
        try {
            if (out!= null) {
                out.writeObject(new Response(ResponseType.NOTIFICATION, "Nueva notificación", notification));
                out.flush(); // Asegurarse de que el objeto se envíe inmediatamente
            }
        } catch (IOException e) {
            System.err.println("Error al enviar notificación a " + (currentUser!= null? currentUser.getUsername() : "cliente desconocido") + ": " + e.getMessage());
            // Si hay un error, posiblemente el cliente se desconectó.
            // Esto debería manejarse con lógica de desconexión.
        }
    }

    @Override
    public void run() {
        try {
            // Los ObjectOutputStream deben crearse antes que los ObjectInputStream del otro lado para evitar deadlocks
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

            // Registrar esta sesión en el servicio para que pueda recibir notificaciones push
            notificationService.addSession(this);

            out.writeObject(new Response(ResponseType.SUCCESS, "Bienvenido al Sistema de Notificaciones. Por favor, LOGIN o REGISTER."));

            Request request;
            while ((request = (Request) in.readObject())!= null) {
                System.out.println("Request de " + clientSocket.getInetAddress().getHostAddress() + " (" + (currentUser!= null? currentUser.getUsername() : "unauthenticated") + "): " + request.getCommand());
                processRequest(request);
                if (request.getCommand() == common.protocol.CommandType.LOGOUT) {
                    break;
                }
            }
        } catch (EOFException e) {
            System.out.println("Cliente " + (currentUser!= null? currentUser.getUsername() : clientSocket.getInetAddress().getHostAddress()) + " se desconectó inesperadamente.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error en la sesión del cliente " + (currentUser!= null? currentUser.getUsername() : clientSocket.getInetAddress().getHostAddress()) + ": " + e.getMessage());
            // e.printStackTrace(); // Descomentar para depuración
        } finally {
            cleanupSession();
        }
    }

    private void processRequest(Request request) throws IOException {
        Response response = null;
        try {
            switch (request.getCommand()) {
                case REGISTER:
                    response = notificationService.registerUser(request.getData());
                    break;
                case LOGIN:
                    response = notificationService.loginUser(request.getData());
                    if (response.getType() == ResponseType.SUCCESS) {
                        this.currentUser = (User) response.getPayload(); // Guarda el usuario logueado
                        notificationService.updateSessionUser(this.currentUser, this);
                    }
                    break;
                case SUBSCRIBE:
                    if (currentUser!= null) {
                        response = notificationService.subscribe(currentUser, (String) request.getData().get("topic"));
                    } else {
                        response = new Response(ResponseType.ERROR, "Debes iniciar sesión para suscribirte.");
                    }
                    break;
                case UNSUBSCRIBE:
                    if (currentUser!= null) {
                        response = notificationService.unsubscribe(currentUser, (String) request.getData().get("topic"));
                    } else {
                        response = new Response(ResponseType.ERROR, "Debes iniciar sesión para desuscribirte.");
                    }
                    break;
                case PUBLISH:
                    if (currentUser!= null) {
                        String topic = (String) request.getData().get("topic");
                        String message = (String) request.getData().get("message");
                        response = notificationService.publish(currentUser, topic, message);
                    } else {
                        response = new Response(ResponseType.ERROR, "Debes iniciar sesión para publicar.");
                    }
                    break;
                case GET_TOPICS:
                    response = notificationService.getAvailableTopics();
                    break;
                case HEARTBEAT:
                    response = new Response(ResponseType.SUCCESS, "Heartbeat recibido.");
                    break;
                case LOGOUT:
                    if (currentUser!= null) {
                        notificationService.removeSession(this);
                        notificationService.logoutUser(currentUser);
                        response = new Response(ResponseType.SUCCESS, "Sesión cerrada.");
                        this.currentUser = null;
                    } else {
                        response = new Response(ResponseType.ERROR, "No hay sesión activa para cerrar.");
                    }
                    break;
                default:
                    response = new Response(ResponseType.ERROR, "Comando desconocido.");
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error procesando request para " + (currentUser!= null? currentUser.getUsername() : "unauthenticated") + ": " + e.getMessage());
            response = new Response(ResponseType.ERROR, "Error interno del servidor: " + e.getMessage());
        }
        if (response!= null) {
            out.writeObject(response);
            out.flush();
        }
    }

    private void cleanupSession() {
        if (currentUser!= null) {
            notificationService.logoutUser(currentUser);
            System.out.println("Usuario " + currentUser.getUsername() + " desconectado.");
        } else {
            System.out.println("Cliente " + clientSocket.getInetAddress().getHostAddress() + " desconectado.");
        }
        notificationService.removeSession(this); // Remover esta sesión del servicio
        try {
            if (in!= null) in.close();
            if (out!= null) out.close();
            if (clientSocket!= null) clientSocket.close();
        } catch (IOException e) {
            System.err.println("Error al cerrar recursos del cliente: " + e.getMessage());
        }
    }
}
