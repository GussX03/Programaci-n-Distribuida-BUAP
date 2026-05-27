package ejemplo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TaskClient extends Remote {
    void notifyResult(String result) throws RemoteException;
}