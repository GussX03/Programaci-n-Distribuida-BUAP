package ejemplo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AuthInterface extends Remote {
    boolean validar(String user, String pass) throws RemoteException;
}