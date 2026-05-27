package ejemplo;

import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;

public class ServidorEcho implements Echo {
    public String repetir(String mensaje) { return "Eco: " + mensaje.toUpperCase(); }

    public static void main(String[] args) {
        try {
            Echo stub = (Echo) UnicastRemoteObject.exportObject(new ServidorEcho(), 0);
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("EchoService", stub);
            System.out.println("Servidor Echo listo.");
        } catch (Exception e) { e.printStackTrace(); }
    }
}