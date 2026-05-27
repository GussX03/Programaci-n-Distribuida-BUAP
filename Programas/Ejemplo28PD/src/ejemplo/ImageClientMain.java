package ejemplo;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ImageClientMain {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            ImageServer server = (ImageServer) registry.lookup("ImageServer");

            ImageClientImpl client = new ImageClientImpl("Cliente1");
            server.registerClient(client);

            // Simulación: enviar datos de imagen como arreglo de bytes
            byte[] fakeImage = new byte[1024]; 
            server.processImage(fakeImage, "grayscale");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}