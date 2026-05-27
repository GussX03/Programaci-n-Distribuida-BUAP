package ejemplo;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class TaskServerMain {
    public static void main(String[] args) {
        try {
            TaskServerImpl server = new TaskServerImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("TaskServer", server);
            System.out.println("Servidor de tareas listo.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}