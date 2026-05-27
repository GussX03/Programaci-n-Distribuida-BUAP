package server.repository;

import java.io.*;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SubscriptionRepository {
    private static final String SUBSCRIPTIONS_FILE = "subscriptions.dat";

    public SubscriptionRepository() throws IOException {
        // Constructor vacío, la carga se realiza al llamar loadSubscriptions
    }

    // Carga suscripciones desde el archivo
    public Map<String, Set<String>> loadSubscriptions() throws IOException {
        File file = new File(SUBSCRIPTIONS_FILE);
        if (!file.exists()) {
            System.out.println("Archivo de suscripciones no encontrado, creando uno nuevo.");
            file.createNewFile();
            return new ConcurrentHashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SUBSCRIPTIONS_FILE))) {
            Map<String, Set<String>> subscriptions = (Map<String, Set<String>>) ois.readObject();
            System.out.println("Suscripciones cargadas.");
            return subscriptions;
        } catch (EOFException e) {
            System.out.println("Archivo de suscripciones vacío.");
            return new ConcurrentHashMap<>();
        } catch (ClassNotFoundException | IOException e) {
            System.err.println("Error al cargar suscripciones: " + e.getMessage());
            throw new IOException("No se pudieron cargar las suscripciones: " + e.getMessage(), e);
        }
    }

    // Guarda suscripciones en el archivo
    public void saveSubscriptions(Map<String, Set<String>> subscriptions) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SUBSCRIPTIONS_FILE))) {
            oos.writeObject(subscriptions);
            System.out.println("Suscripciones guardadas en " + SUBSCRIPTIONS_FILE);
        }
    }
}
