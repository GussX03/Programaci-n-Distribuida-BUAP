package ejemplo;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServidorAereo {
    public static void main(String[] args) {
        try {
            ReservaInterface motor = new ReservaImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("SistemaVuelos", motor);
            System.out.println("Servidor de Aerolínea operativo.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}