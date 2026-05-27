package ejemplo;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class TaskClientImpl extends UnicastRemoteObject implements TaskClient {
    private final String name;

    protected TaskClientImpl(String name) throws RemoteException {
        this.name = name;
    }

    @Override
    public void notifyResult(String result) throws RemoteException {
        System.out.println(name + " recibió resultado: " + result);
    }
}