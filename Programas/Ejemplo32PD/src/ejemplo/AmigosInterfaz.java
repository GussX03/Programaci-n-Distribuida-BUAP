package ejemplo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AmigosInterfaz extends Remote {
    boolean sonAmigos(int num1, int num2) throws RemoteException;
}
