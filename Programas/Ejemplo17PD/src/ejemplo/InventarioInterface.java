package ejemplo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InventarioInterface extends Remote {
    String comprar(String producto, int cantidad) throws RemoteException;
    int consultarStock(String producto) throws RemoteException;
}
