package ejemplo;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class InventarioCliente {
    public static void main(String[] args) {
        try {
            Registry registro = LocateRegistry.getRegistry("localhost", 1099);
            InventarioInterface stub = (InventarioInterface) registro.lookup("ServicioInventario");
            
            System.out.println("Stock de Consola: " + stub.consultarStock("Consola"));
            System.out.println(stub.comprar("Consola", 2));
        } catch (Exception e) { e.printStackTrace(); }
    }
}
