package ejemplo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class SubastaImpl extends UnicastRemoteObject implements InterfaceSubasta {
    private List<Producto> productos;

    protected SubastaImpl() throws RemoteException {
        productos = new ArrayList<>();
        productos.add(new Producto("Laptop Gamer", 1000.0));
        productos.add(new Producto("iPhone 15", 800.0));
    }

    @Override
    public List<Producto> obtenerCatalogo() throws RemoteException {
        return productos;
    }

    @Override
    public synchronized boolean ofertar(String nombreProducto, String postor, double cantidad) throws RemoteException {
        for (Producto p : productos) {
            if (p.getNombre().equals(nombreProducto)) {
                // Regla de negocio: la oferta debe ser superior a la actual
                if (cantidad > p.getPrecioActual()) {
                    p.setPrecioActual(cantidad);
                    p.setLiderActual(postor);
                    System.out.println("Nueva oferta aceptada: " + p);
                    return true;
                }
            }
        }
        return false;
    }
}