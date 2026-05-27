package ejemplo;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;

public class AuthServidor extends UnicastRemoteObject implements AuthInterface {
    public AuthServidor() throws java.rmi.RemoteException {}
    
    public boolean validar(String u, String p) {
        return "root".equals(u) && "secret123".equals(p);
    }

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099).rebind("Auth", new AuthServidor());
            System.out.println("Servidor de autenticación listo.");
        } catch (Exception e) { e.printStackTrace(); }
    }
}