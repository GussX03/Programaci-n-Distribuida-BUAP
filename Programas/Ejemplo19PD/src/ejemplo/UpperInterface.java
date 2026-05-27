package ejemplo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UpperInterface extends Remote {
    String transformar(String texto) throws RemoteException;
}