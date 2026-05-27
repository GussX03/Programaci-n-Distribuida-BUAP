package ejemplo;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClienteAuth {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            Autenticador stub = (Autenticador) registry.lookup("AuthService");  // ← CORREGIDO
            System.out.println("Acceso concedido? " + stub.login("admin", "1234"));
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
    }
}