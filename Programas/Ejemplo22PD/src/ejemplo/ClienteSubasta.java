package ejemplo;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

public class ClienteSubasta {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            InterfaceSubasta stub = (InterfaceSubasta) registry.lookup("SubastaService");
            
            Scanner sc = new Scanner(System.in);
            System.out.print("Introduce tu nombre de postor: ");
            String nombrePostor = sc.nextLine();

            // Ciclo simple de subasta
            while (true) {
                List<Producto> lista = stub.obtenerCatalogo();
                System.out.println("\n--- Productos en Subasta ---");
                for (int i = 0; i < lista.size(); i++) {
                    System.out.println(i + ". " + lista.get(i));
                }

                System.out.print("\nElige el índice del producto para ofertar (o -1 para salir): ");
                int seleccion = sc.nextInt();
                if (seleccion == -1) break;

                System.out.print("Tu oferta: ");
                double puja = sc.nextDouble();

                boolean exito = stub.ofertar(lista.get(seleccion).getNombre(), nombrePostor, puja);
                
                if (exito) {
                    System.out.println("¡Vas ganando la subasta!");
                } else {
                    System.out.println("Oferta rechazada. Alguien ofreció más o el precio subió.");
                }
            }
        } catch (Exception e) {
            System.err.println("Error en el cliente: " + e.toString());
        }
    }
}