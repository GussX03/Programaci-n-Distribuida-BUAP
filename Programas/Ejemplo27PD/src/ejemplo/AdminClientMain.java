package ejemplo;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class AdminClientMain {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            SensorServer server = (SensorServer) registry.lookup("SensorServer");

            AdminClientImpl admin = new AdminClientImpl("Admin1");
            server.registerAdmin(admin);

            System.out.println("Admin conectado y esperando alertas...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}