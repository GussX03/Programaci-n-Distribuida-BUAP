package ejemplo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AdminClient extends Remote {
    void notifyAlert(String alert) throws RemoteException;
}