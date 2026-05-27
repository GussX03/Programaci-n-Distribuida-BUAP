package ejemplo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Echo extends Remote {
    String repetir(String mensaje) throws RemoteException;
}