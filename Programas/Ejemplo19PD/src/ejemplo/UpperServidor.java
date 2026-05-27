package ejemplo;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;

public class UpperServidor extends UnicastRemoteObject implements UpperInterface {
    public UpperServidor() throws java.rmi.RemoteException {}
    public String transformar(String t) { return t.toUpperCase(); }

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099).rebind("UpperService", new UpperServidor());
        } catch (Exception e) { e.printStackTrace(); }
    }
}