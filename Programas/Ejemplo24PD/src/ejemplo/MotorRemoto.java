package ejemplo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MotorRemoto extends Remote {
    <T> T ejecutarTarea(Tarea<T> t) throws RemoteException;
}