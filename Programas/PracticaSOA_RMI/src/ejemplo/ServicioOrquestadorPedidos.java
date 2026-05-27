package ejemplo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicioOrquestadorPedidos extends Remote {

    String procesarOrdenIndustrial(String idArticulo, int cantidad)
            throws RemoteException;
}