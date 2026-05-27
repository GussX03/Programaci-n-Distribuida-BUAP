package ejemplo;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerInventarioMain {
    public static void main(String[] args) {
        try {
            InventarioImpl objetoRemoto = new InventarioImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("ServicioInventario", objetoRemoto);
            System.out.println("Servidor de Inventario activo y monitoreando stock...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}