package ejemplo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class InventarioImpl extends UnicastRemoteObject implements InventarioInterface {
    private Map<String, Producto> almacen;
    private final int UMBRAL_ALERTA = 3;

    protected InventarioImpl() throws RemoteException {
        almacen = new ConcurrentHashMap<>();
        almacen.put("Laptop", new Producto("Laptop", 10));
        almacen.put("Mouse", new Producto("Mouse", 5));
    }

    @Override
    public Map<String, Producto> obtenerInventarioCompleto() throws RemoteException {
        return almacen;
    }

    @Override
    public synchronized String realizarVenta(String nombre, int cant) throws RemoteException {
        Producto p = almacen.get(nombre);
        
        if (p == null) return "Error: Producto no existe.";
        
        if (p.getCantidad() >= cant) {
            p.setCantidad(p.getCantidad() - cant);
            
            String msg = "Venta exitosa. Quedan: " + p.getCantidad();
            
            // Lógica de Alerta de Stock Bajo
            if (p.getCantidad() <= UMBRAL_ALERTA) {
                System.out.println("!!! ALERTA DE SERVIDOR: Stock crítico de " + nombre);
                msg += " [AVISO: STOCK BAJO]";
            }
            return msg;
        } else {
            return "Venta rechazada: Stock insuficiente (Solo hay " + p.getCantidad() + ")";
        }
    }
}