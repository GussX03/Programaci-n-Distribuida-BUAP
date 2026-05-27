package ejemplo;

import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ServidorTareas implements Tareas {
    private List<String> lista = new ArrayList<>();
    public void añadirTarea(String t) { lista.add(t); }
    public List<String> verTareas() { return lista; }

    public static void main(String[] args) {
        try {
            Tareas stub = (Tareas) UnicastRemoteObject.exportObject(new ServidorTareas(), 0);
            LocateRegistry.createRegistry(1099).rebind("TaskService", stub);
            System.out.println("Servidor de Tareas iniciado.");
        } catch (Exception e) { e.printStackTrace(); }
    }
}