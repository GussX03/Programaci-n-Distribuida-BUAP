package ejemplo;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface InterfaceSubasta extends Remote {
    // Retorna la lista de productos disponibles
    List<Producto> obtenerCatalogo() throws RemoteException;
    
    // Proceso de puja: devuelve true si la oferta fue aceptada
    boolean ofertar(String nombreProducto, String postor, double cantidad) throws RemoteException;
}