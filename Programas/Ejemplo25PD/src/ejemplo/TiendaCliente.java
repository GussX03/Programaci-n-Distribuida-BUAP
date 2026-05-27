package ejemplo;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class TiendaCliente {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            InventarioInterface inventario = (InventarioInterface) registry.lookup("ServicioInventario");
            
            Scanner sc = new Scanner(System.in);
            
            System.out.println("--- Almacén Conectado ---");
            inventario.obtenerInventarioCompleto().forEach((k, v) -> System.out.println(v));

            System.out.print("\nProducto a comprar: ");
            String prod = sc.nextLine();
            System.out.print("Cantidad: ");
            int cant = sc.nextInt();

            String respuesta = inventario.realizarVenta(prod, cant);
            System.out.println("Respuesta del servidor: " + respuesta);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}