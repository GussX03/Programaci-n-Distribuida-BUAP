package ejemplo;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class AdminClientImpl extends UnicastRemoteObject implements AdminClient {
    private final String name;

    protected AdminClientImpl(String name) throws RemoteException {
        this.name = name;
    }

    @Override
    public void notifyAlert(String alert) throws RemoteException {
        System.out.println(name + " recibió notificación: " + alert);
    }
}