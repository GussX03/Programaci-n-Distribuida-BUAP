package ejemplo;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class TaskClientMain {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            TaskServer server = (TaskServer) registry.lookup("TaskServer");

            TaskClientImpl client = new TaskClientImpl("Cliente1");
            server.registerClient(client);

            // Enviar tareas con diferentes prioridades
            server.submitTask("Generar reporte", 1, client);
            server.submitTask("Analizar logs críticos", 5, client);
            server.submitTask("Backup de base de datos", 3, client);

            // Espera breve para recibir callbacks antes de terminar el proceso
            Thread.sleep(7000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
