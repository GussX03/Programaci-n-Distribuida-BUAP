package ejemplo;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;

public class InventarioServidor extends UnicastRemoteObject implements InventarioInterface {
    private ConcurrentHashMap<String, Integer> productos = new ConcurrentHashMap<>();

    protected InventarioServidor() throws RemoteException {
        productos.put("Consola", 5);
        productos.put("Celular", 10);
    }

    // El uso de synchronized evita que dos clientes compren el último objeto a la vez
    public synchronized String comprar(String producto, int cantidad) throws RemoteException {
        int actual = productos.getOrDefault(producto, 0);
        if (actual >= cantidad) {
            productos.put(producto, actual - cantidad);
            return "Venta realizada. Quedan " + (actual - cantidad) + " unidades.";
        }
        return "Error: Stock insuficiente (" + actual + " disponibles).";
    }

    public int consultarStock(String producto) throws RemoteException {
        return productos.getOrDefault(producto, 0);
    }

    public static void main(String[] args) {
        try {
            Registry registro = LocateRegistry.createRegistry(1099);
            registro.rebind("ServicioInventario", new InventarioServidor());
            System.out.println("Servidor de Inventario listo.");
        } catch (Exception e) { e.printStackTrace(); }
    }
}
