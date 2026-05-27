package ejemplo;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;  // ← ESTE FALTA

public class ServidorAuth implements Autenticador {
    
    public ServidorAuth() throws RemoteException {
        // Constructor necesario
    }
    
    public boolean login(String u, String p) throws RemoteException { 
        return u.equals("admin") && p.equals("1234"); 
    }

    public static void main(String[] args) {
        try {
            ServidorAuth server = new ServidorAuth();
            Autenticador stub = (Autenticador) UnicastRemoteObject.exportObject(server, 0);
            LocateRegistry.createRegistry(1099).rebind("AuthService", stub);
            System.out.println("Servidor de Autenticación activo.");
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
    }
}