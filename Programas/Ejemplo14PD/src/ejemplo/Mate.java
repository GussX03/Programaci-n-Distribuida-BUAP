package ejemplo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Mate extends Remote {
    long factorial(int n) throws RemoteException;
}