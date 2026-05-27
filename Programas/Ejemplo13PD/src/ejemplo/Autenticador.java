package ejemplo;

import java.rmi.Remote;
import java.rmi.RemoteException;  // ← ESTE FALTA

public interface Autenticador extends Remote {
    boolean login(String user, String pass) throws RemoteException;
}