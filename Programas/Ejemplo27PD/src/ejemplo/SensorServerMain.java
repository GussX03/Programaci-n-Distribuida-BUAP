package ejemplo;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class SensorServerMain {
    public static void main(String[] args) {
        try {
            SensorServerImpl server = new SensorServerImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("SensorServer", server);
            System.out.println("Servidor de sensores listo.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}