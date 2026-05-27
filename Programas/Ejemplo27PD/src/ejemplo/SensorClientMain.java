package ejemplo;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class SensorClientMain {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            SensorServer server = (SensorServer) registry.lookup("SensorServer");

            // Simulación de lecturas
            server.sendReading("SensorA", 45.0);
            server.sendReading("SensorB", 60.5); // dispara alerta
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}