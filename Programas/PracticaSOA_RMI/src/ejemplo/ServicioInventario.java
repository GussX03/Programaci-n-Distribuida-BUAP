package ejemplo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicioInventario extends Remote {

    boolean verificarYReducirStock(String idArticulo, int cantidad)
            throws RemoteException;

    int obtenerStock(String idArticulo)
            throws RemoteException;
}