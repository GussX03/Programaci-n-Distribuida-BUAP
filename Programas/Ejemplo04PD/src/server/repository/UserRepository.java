package server.repository;

import common.models.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepository {
    private static final String USERS_FILE = "users.dat";
    private Map<String, User> users; // Mapa para acceso rápido por username

    public UserRepository() throws IOException {
        users = new ConcurrentHashMap<>();
        loadUsers();
    }

    // Carga usuarios desde el archivo
    public List<User> loadUsers() throws IOException {
        File file = new File(USERS_FILE);
        if (!file.exists()) {
            System.out.println("Archivo de usuarios no encontrado, creando uno nuevo.");
            file.createNewFile();
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USERS_FILE))) {
            users = (Map<String, User>) ois.readObject();
            System.out.println("Usuarios cargados: " + users.size());
            return new ArrayList<>(users.values());
        } catch (EOFException e) {
            System.out.println("Archivo de usuarios vacío.");
            users = new ConcurrentHashMap<>();
            return new ArrayList<>();
        } catch (ClassNotFoundException | IOException e) {
            System.err.println("Error al cargar usuarios: " + e.getMessage());
            // Si hay un error, inicializar users para evitar NullPointerException
            users = new ConcurrentHashMap<>();
            throw new IOException("No se pudieron cargar los usuarios: " + e.getMessage(), e);
        }
    }

    // Guarda usuarios en el archivo
    public void saveUsers() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            oos.writeObject(users);
            System.out.println("Usuarios guardados en " + USERS_FILE);
        }
    }

    public User getUserByUsername(String username) {
        return users.get(username);
    }

    public void addUser(User user) {
        users.put(user.getUsername(), user);
    }
    // No se necesita removeUser en este ejemplo, pero podría añadirse
}
