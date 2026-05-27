package ejemplo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Termometro extends Remote {
    double getTemperatura() throws RemoteException;
}
