package ejemplo;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerSubasta {
    public static void main(String[] args) {
        try {
            InterfaceSubasta server = new SubastaImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("SubastaService", server);
            System.out.println("Servidor de Subastas iniciado correctamente...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}