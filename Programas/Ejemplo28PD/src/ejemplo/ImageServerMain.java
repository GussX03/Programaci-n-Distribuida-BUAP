package ejemplo;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ImageServerMain {
    public static void main(String[] args) {
        try {
            ImageServerImpl server = new ImageServerImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("ImageServer", server);
            System.out.println("Servidor de imágenes listo.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}