package ejemplo;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface InventarioInterface extends Remote {
    Map<String, Producto> obtenerInventarioCompleto() throws RemoteException;
    
    // Devuelve un mensaje de estado tras la venta
    String realizarVenta(String nombreProducto, int cantidad) throws RemoteException;
}