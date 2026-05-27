package ejemplo;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;

public class AppServicioInventario extends UnicastRemoteObject implements ServicioInventario {
    
    // Almacenamiento autónomo de datos (Thred-safe para concurrencia remota)
    private ConcurrentHashMap<String, Integer> stockAlmacen;

    public AppServicioInventario() throws RemoteException {
        super();
        stockAlmacen = new ConcurrentHashMap<>();
        // Carga inicial de stock para pruebas (Artículos industriales)
        stockAlmacen.put("PLC-SIEMENS-S7", 15);
        stockAlmacen.put("MOTOR-TRIFASICO-2HP", 5);
        stockAlmacen.put("SENSOR-LASER-X3", 50);
    }

    @Override
    public synchronized boolean verificarYReducirStock(String idArticulo, int cantidad) throws RemoteException {
        if (!stockAlmacen.containsKey(idArticulo)) {
            return false;
        }
        int stockActual = stockAlmacen.get(idArticulo);
        if (stockActual >= cantidad) {
            stockAlmacen.put(idArticulo, stockActual - cantidad);
            System.out.println("[INVENTARIO] Stock reducido para " + idArticulo + ". Nuevo stock: " + (stockActual - cantidad));
            return true;
        }
        System.out.println("[INVENTARIO] Solicitud rechazada. Stock insuficiente para " + idArticulo);
        return false;
    }

    @Override
    public synchronized int obtenerStock(String idArticulo) throws RemoteException {
        return stockAlmacen.getOrDefault(idArticulo, 0);
    }

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            AppServicioInventario servicio = new AppServicioInventario();
            
            registry.rebind("ServicioInventario", servicio);
            System.out.println(">>> AppServicioInventario desplegada y registrada exitosamente.");
        } catch (Exception e) {
            System.err.println("Error al iniciar el Servicio de Inventario: " + e.getMessage());
            e.printStackTrace();
        }
    }
}