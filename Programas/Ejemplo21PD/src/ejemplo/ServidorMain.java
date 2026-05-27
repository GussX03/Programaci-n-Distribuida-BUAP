package ejemplo;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServidorMain {
    public static void main(String[] args) {
        try {
            ServicioSeguimiento motor = new SeguimientoImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("CourierService", motor);
            System.out.println("Servidor de Seguimiento listo...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}