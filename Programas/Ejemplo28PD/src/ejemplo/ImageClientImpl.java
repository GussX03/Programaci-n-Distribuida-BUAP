package ejemplo;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class ImageClientImpl extends UnicastRemoteObject implements ImageClient {
    private final String name;

    protected ImageClientImpl(String name) throws RemoteException {
        this.name = name;
    }

    @Override
    public void notifyProcessed(byte[] processedImage) throws RemoteException {
        System.out.println(name + " recibió imagen procesada (" + processedImage.length + " bytes).");
    }
}