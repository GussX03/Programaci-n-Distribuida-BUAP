package ejemplo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ImageClient extends Remote {
    void notifyProcessed(byte[] processedImage) throws RemoteException;
}