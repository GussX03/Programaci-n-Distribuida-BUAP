package server.service;

import common.models.Notification;
import common.models.User;
import common.protocol.Response;
import common.protocol.ResponseType;
import server.repository.SubscriptionRepository;
import server.repository.UserRepository;
import server.session.ClientSessionHandler;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class NotificationService {
    private UserRepository userRepository;
    private SubscriptionRepository subscriptionRepository;
    // Map para mantener un registro de las sesiones activas, mapeando usuario a su manejador
    private Map<String, ClientSessionHandler> activeSessions = new ConcurrentHashMap<>();
    // Map para almacenar las suscripciones (Tema -> Set de Usernames)
    private Map<String, Set<String>> topicSubscriptions = new ConcurrentHashMap<>();
    // Temas disponibles
    private Set<String> availableTopics = ConcurrentHashMap.newKeySet();

    public NotificationService() {
        try {
            this.userRepository = new UserRepository();
            this.subscriptionRepository = new SubscriptionRepository();
            loadInitialData(); // Carga usuarios y suscripciones al inicio
        } catch (IOException e) {
            System.err.println("Error al inicializar repositorios: " + e.getMessage());
            // Considerar si es un error fatal y el servidor debe detenerse
        }
    }

    private void loadInitialData() throws IOException {
        System.out.println("Cargando datos de usuarios y suscripciones...");
        userRepository.loadUsers().forEach(user -> {
            // Si el servidor se reinicia, los usuarios no están activos inicialmente
            System.out.println("Usuario cargado: " + user.getUsername());
        });

        // Cargar suscripciones y popular topicSubscriptions
        for (Map.Entry<String, Set<String>> entry : subscriptionRepository.loadSubscriptions().entrySet()) {

            Set<String> subscribers = ConcurrentHashMap.newKeySet();
            subscribers.addAll(entry.getValue());

            topicSubscriptions.put(entry.getKey(), subscribers);
            availableTopics.add(entry.getKey());

            System.out.println("Tema '" + entry.getKey() + "' cargado con suscriptores: " + entry.getValue());
        }
    }

    // --- Métodos de gestión de Sesiones ---
    public void addSession(ClientSessionHandler sessionHandler) {
        // En este punto el usuario aún no está logueado, solo se añade el manejador
        // La sesión se mapeará a un usuario cuando se loguee
    }

    public void removeSession(ClientSessionHandler sessionHandler) {
        if (sessionHandler.getCurrentUser()!= null) {
            activeSessions.remove(sessionHandler.getCurrentUser().getUsername());
            System.out.println("Sesión de " + sessionHandler.getCurrentUser().getUsername() + " eliminada.");
        }
    }

    public void updateSessionUser(User user, ClientSessionHandler sessionHandler) {
        activeSessions.put(user.getUsername(), sessionHandler);
        System.out.println("Usuario " + user.getUsername() + " asociado a su sesión.");
    }

    // --- Métodos de Lógica de Negocio ---

    public Response registerUser(Map<String, Object> data) throws IOException {
        String username = (String) data.get("username");
        String password = (String) data.get("password");

        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            return new Response(ResponseType.ERROR, "Nombre de usuario y contraseña no pueden estar vacíos.");
        }

        if (userRepository.getUserByUsername(username)!= null) {
            return new Response(ResponseType.ERROR, "El nombre de usuario ya existe.");
        }

        User newUser = new User(username, password);
        userRepository.addUser(newUser);
        userRepository.saveUsers(); // Guardar cambios
        System.out.println("Nuevo usuario registrado: " + username);
        return new Response(ResponseType.SUCCESS, "Usuario registrado exitosamente.", newUser);
    }

    public Response loginUser(Map<String, Object> data) {
        String username = (String) data.get("username");
        String password = (String) data.get("password");

        User user = userRepository.getUserByUsername(username);
        if (user == null ||!user.getPassword().equals(password)) {
            return new Response(ResponseType.ERROR, "Nombre de usuario o contraseña incorrectos.");
        }

        if (activeSessions.containsKey(username)) {
            return new Response(ResponseType.ERROR, "El usuario ya está logueado en otra sesión.");
        }

        // Si el login es exitoso, la sesión del ClientSessionHandler se actualizará con este usuario
        // (gestionado en ClientSessionHandler, no aquí directamente)
        System.out.println("Usuario logueado: " + username);
        return new Response(ResponseType.SUCCESS, "Inicio de sesión exitoso.", user);
    }

    public void logoutUser(User user) {
        // La sesión se eliminará en cleanupSession de ClientSessionHandler
        // Aquí solo limpiamos si hay alguna otra lógica específica de logout
    }

    public Response subscribe(User user, String topic) throws IOException {
        if (topic == null || topic.trim().isEmpty()) {
            return new Response(ResponseType.ERROR, "El tema no puede estar vacío.");
        }
        availableTopics.add(topic); // Asegurar que el tema existe

        topicSubscriptions.computeIfAbsent(topic, k -> ConcurrentHashMap.newKeySet()).add(user.getUsername());
        subscriptionRepository.saveSubscriptions(topicSubscriptions); // Guardar cambios
        System.out.println(user.getUsername() + " suscrito a '" + topic + "'");
        return new Response(ResponseType.SUCCESS, "Suscrito al tema '" + topic + "' exitosamente.");
    }

    public Response unsubscribe(User user, String topic) throws IOException {
        if (topic == null || topic.trim().isEmpty()) {
            return new Response(ResponseType.ERROR, "El tema no puede estar vacío.");
        }

        Set<String> subscribers = topicSubscriptions.get(topic);
        if (subscribers!= null && subscribers.remove(user.getUsername())) {
            if (subscribers.isEmpty()) {
                topicSubscriptions.remove(topic); // Opcional: remover tema si no tiene suscriptores
            }
            subscriptionRepository.saveSubscriptions(topicSubscriptions); // Guardar cambios
            System.out.println(user.getUsername() + " desuscrito de '" + topic + "'");
            return new Response(ResponseType.SUCCESS, "Desuscrito del tema '" + topic + "' exitosamente.");
        }
        return new Response(ResponseType.ERROR, "No estabas suscrito al tema '" + topic + "'.");
    }

    public Response publish(User publisher, String topic, String message) {
        if (topic == null || topic.trim().isEmpty() || message == null || message.trim().isEmpty()) {
            return new Response(ResponseType.ERROR, "El tema y el mensaje no pueden estar vacíos.");
        }

        if (!availableTopics.contains(topic)) {
            return new Response(ResponseType.ERROR, "El tema '" + topic + "' no existe.");
        }

        Notification notification = new Notification(topic, message, publisher.getUsername());
        Set<String> subscribers = topicSubscriptions.get(topic);

        if (subscribers == null || subscribers.isEmpty()) {
            System.out.println("Notificación publicada en '" + topic + "' por " + publisher.getUsername() + ", pero no hay suscriptores.");
            return new Response(ResponseType.SUCCESS, "Notificación publicada, pero no hay suscriptores en este tema.");
        }

        System.out.println("Notificación publicada en '" + topic + "' por " + publisher.getUsername() + ". Enviando a " + subscribers.size() + " suscriptores.");
        for (String subscriberUsername : subscribers) {
            ClientSessionHandler session = activeSessions.get(subscriberUsername);
            if (session!= null) {
                session.sendNotification(notification); // Envía la notificación PUSH
                System.out.println(" -> Enviada a: " + subscriberUsername);
            } else {
                System.out.println(" -> Suscriptor " + subscriberUsername + " no está activo.");
                // Podríamos guardar notificaciones offline aquí.
            }
        }
        return new Response(ResponseType.SUCCESS, "Notificación publicada y enviada a los suscriptores activos.");
    }

    public Response getAvailableTopics() {
        // En un sistema real, no enviaríamos el Set directamente sino una copia o un List
        return new Response(ResponseType.SUCCESS, "Temas disponibles.", new ArrayList<>(availableTopics));
    }
}
