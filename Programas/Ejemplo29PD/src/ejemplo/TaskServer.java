package ejemplo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TaskServer extends Remote {
    void registerClient(TaskClient client) throws RemoteException;
    void submitTask(String description, int priority, TaskClient client) throws RemoteException;
}
