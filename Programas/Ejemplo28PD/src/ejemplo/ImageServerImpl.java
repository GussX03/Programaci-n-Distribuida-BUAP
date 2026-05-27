package ejemplo;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ImageServerImpl extends UnicastRemoteObject implements ImageServer {
    private final List<ImageClient> clients;

    protected ImageServerImpl() throws RemoteException {
        clients = new ArrayList<>();
    }

    @Override
    public synchronized void registerClient(ImageClient client) throws RemoteException {
        clients.add(client);
        System.out.println("Cliente registrado.");
    }

    @Override
    public synchronized void processImage(byte[] imageData, String filter) throws RemoteException {
        new Thread(() -> {
            try {
                // Simulación de procesamiento de imagen
                byte[] result = applyFilter(imageData, filter);
                for (ImageClient client : clients) {
                    client.notifyProcessed(result);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private byte[] applyFilter(byte[] imageData, String filter) {
        // Aquí iría la lógica real de procesamiento (ej. OpenCV)
        System.out.println("Aplicando filtro: " + filter);
        return imageData; // Simulación: devuelve la misma imagen
    }
}