package ejemplo;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;

public class NotificadorServidor extends UnicastRemoteObject implements NotificadorInterface {
    private ArrayList<String> usuarios = new ArrayList<>();

    public NotificadorServidor() throws java.rmi.RemoteException {}

    public void registrarCliente(String nombre) {
        usuarios.add(nombre);
        System.out.println("Usuario registrado: " + nombre);
    }

    public void enviarMensaje(String msg) {
        System.out.println("Difundiendo: " + msg + " a " + usuarios.size() + " usuarios.");
    }

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099).rebind("Notificador", new NotificadorServidor());
            System.out.println("Servidor de notificaciones activo.");
        } catch (Exception e) { e.printStackTrace(); }
    }
}
