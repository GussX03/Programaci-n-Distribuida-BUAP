package ejemplo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ImageServer extends Remote {
    void registerClient(ImageClient client) throws RemoteException;
    void processImage(byte[] imageData, String filter) throws RemoteException;
}