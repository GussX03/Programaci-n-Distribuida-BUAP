package ejemplo;

import java.rmi.registry.*;

public class ClienteTareas {
    public static void main(String[] args) {
        try {
            Tareas stub = (Tareas) LocateRegistry.getRegistry("localhost", 1099).lookup("TaskService");
            stub.añadirTarea("Estudiar Java RMI");
            stub.añadirTarea("Comprar café");
            System.out.println("Mis tareas: " + stub.verTareas());
        } catch (Exception e) { e.printStackTrace(); }
    }
}