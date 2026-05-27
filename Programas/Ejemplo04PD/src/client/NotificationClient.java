package client;

import common.models.Notification;
import common.models.User;
import common.protocol.CommandType;
import common.protocol.Request;
import common.protocol.Response;
import common.protocol.ResponseType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class NotificationClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 5002;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private User loggedInUser = null;
    private ServerListener serverListener; // Hilo para escuchar notificaciones push del servidor
    private final BlockingQueue<Response> responseQueue = new LinkedBlockingQueue<>();
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        NotificationClient client = new NotificationClient();
        try {
            client.startClient();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error en el cliente: " + e.getMessage());
            e.printStackTrace();
        } finally {
            client.stopClient();
        }
    }

    public void startClient() throws IOException, ClassNotFoundException {
        socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());

        System.out.println("Conectado al servidor de notificaciones.");

        // Leer el mensaje de bienvenida inicial del servidor
        Response welcomeResponse = (Response) in.readObject();
        if (welcomeResponse.getType() == ResponseType.SUCCESS) {
            System.out.println("Servidor: " + welcomeResponse.getMessage());
        } else {
            System.err.println("Error inicial del servidor: " + welcomeResponse.getMessage());
            stopClient();
            return;
        }
        
        // Iniciar hilo para escuchar mensajes asíncronos del servidor (notificaciones)
        serverListener = new ServerListener(in, this, responseQueue);
        serverListener.start();

        displayHelp();

        while (true) {
            System.out.print("\n> ");
            String commandLine = scanner.nextLine().trim();

            if (commandLine.equalsIgnoreCase("quit")) {
                Request logoutRequest = new Request(CommandType.LOGOUT, new HashMap<>());
                sendRequest(logoutRequest); // Enviar logout antes de cerrar
                break;
            }

            processLocalCommand(commandLine);
        }
    }

    private void processLocalCommand(String commandLine) throws IOException, ClassNotFoundException {
        String[] parts = commandLine.split(" ", 2);
        String commandStr = parts[0].toUpperCase();
        String arg = parts.length > 1? parts[1] : "";

        Request request = null;
        Map<String, Object> data = new HashMap<>();

        switch (commandStr) {
            case "REGISTER":
                if (loggedInUser!= null) { System.out.println("Ya estás logueado."); return; }
                System.out.print("Username: "); String regUser = scanner.nextLine();
                System.out.print("Password: "); String regPass = scanner.nextLine();
                data.put("username", regUser);
                data.put("password", regPass);
                request = new Request(CommandType.REGISTER, data);
                break;
            case "LOGIN":
                if (loggedInUser!= null) { System.out.println("Ya estás logueado."); return; }
                System.out.print("Username: "); String loginUser = scanner.nextLine();
                System.out.print("Password: "); String loginPass = scanner.nextLine();
                data.put("username", loginUser);
                data.put("password", loginPass);
                request = new Request(CommandType.LOGIN, data);
                break;
            case "SUBSCRIBE":
                if (loggedInUser == null) { System.out.println("Debes iniciar sesión."); return; }
                if (arg.isEmpty()) { System.out.println("Uso: SUBSCRIBE <tema>"); return; }
                data.put("topic", arg);
                request = new Request(CommandType.SUBSCRIBE, data);
                break;
            case "UNSUBSCRIBE":
                if (loggedInUser == null) { System.out.println("Debes iniciar sesión."); return; }
                if (arg.isEmpty()) { System.out.println("Uso: UNSUBSCRIBE <tema>"); return; }
                data.put("topic", arg);
                request = new Request(CommandType.UNSUBSCRIBE, data);
                break;
            case "PUBLISH":
                if (loggedInUser == null) { System.out.println("Debes iniciar sesión."); return; }
                if (arg.isEmpty() ||!arg.contains(" ")) { System.out.println("Uso: PUBLISH <tema> <mensaje>"); return; }
                String topic = arg.substring(0, arg.indexOf(" "));
                String message = arg.substring(arg.indexOf(" ") + 1);
                data.put("topic", topic);
                data.put("message", message);
                request = new Request(CommandType.PUBLISH, data);
                break;
            case "TOPICS":
                request = new Request(CommandType.GET_TOPICS, new HashMap<>());
                break;
            case "HELP":
                displayHelp();
                return;
            default:
                System.out.println("Comando desconocido. Escribe 'HELP'.");
                return;
        }

        if (request!= null) {
            Response response = sendRequest(request);
            handleServerResponse(response, request.getCommand());
        }
    }

    private Response sendRequest(Request request) throws IOException {
        out.writeObject(request);
        out.flush();
        try {
            Response response = responseQueue.poll(10, TimeUnit.SECONDS);
            if (response == null) {
                return new Response(ResponseType.ERROR, "Tiempo de espera agotado al esperar respuesta del servidor.");
            }
            return response;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new Response(ResponseType.ERROR, "La espera de respuesta fue interrumpida.");
        }
    }

    private void handleServerResponse(Response response, CommandType commandType) {
        if (response == null) {
            System.err.println("No se recibió respuesta del servidor.");
            return;
        }
        switch (response.getType()) {
            case SUCCESS:
                System.out.println("SUCCESS: " + response.getMessage());
                if (commandType == CommandType.LOGIN && response.getPayload() instanceof User) {
                    this.loggedInUser = (User) response.getPayload();
                    System.out.println("¡Bienvenido, " + loggedInUser.getUsername() + "!");
                } else if (response.getPayload() instanceof List) {
                    System.out.println("Temas disponibles: " + response.getPayload());
                }
                break;
            case ERROR:
                System.err.println("ERROR: " + response.getMessage());
                break;
            case NOTIFICATION:
                // Las notificaciones push se manejan en el ServerListener, no aquí.
                System.out.println("CLIENT-INTERNAL: Notificación inesperada en el hilo principal.");
                break;
        }
    }

    // Método para manejar notificaciones recibidas asíncronamente
    public void handleIncomingNotification(Notification notification) {
        System.out.println("\n*** NOTIFICACIÓN RECIBIDA ***");
        System.out.println(notification);
        System.out.print("> "); // Vuelve a mostrar el prompt después de la notificación
    }

    private void displayHelp() {
        System.out.println("\n--- Comandos del Cliente ---");
        System.out.println("REGISTER - Registra un nuevo usuario.");
        System.out.println("LOGIN - Inicia sesión con un usuario existente.");
        System.out.println("SUBSCRIBE <tema> - Suscribirse a un tema.");
        System.out.println("UNSUBSCRIBE <tema> - Desuscribirse de un tema.");
        System.out.println("PUBLISH <tema> <mensaje> - Publicar una notificación en un tema.");
        System.out.println("TOPICS - Ver la lista de temas disponibles.");
        System.out.println("HELP - Muestra esta ayuda.");
        System.out.println("QUIT - Cierra la conexión y sale.");
        System.out.println("----------------------------");
    }

    public void stopClient() {
        if (serverListener!= null) {
            serverListener.interrupt(); // Detener el hilo de escucha
        }
        try {
            if (out!= null) out.close();
            if (in!= null) in.close();
            if (socket!= null) socket.close();
            System.out.println("Cliente desconectado.");
        } catch (IOException e) {
            System.err.println("Error al cerrar recursos del cliente: " + e.getMessage());
        }
    }
}
