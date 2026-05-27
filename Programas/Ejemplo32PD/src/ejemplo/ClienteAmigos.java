package ejemplo;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class ClienteAmigos {
    
    public static void main(String[] args) {
        String host = "localhost"; // Cambiar si es necesario
        
        try {
            // Obtener el registro RMI
            Registry registry = LocateRegistry.getRegistry(host, 1099);
            
            // Buscar el objeto remoto por su nombre
            AmigosInterfaz servicio = (AmigosInterfaz) registry.lookup("ServicioAmigos");
            
            System.out.println("=== Verificador de Números Amigos ===");
            System.out.println("Dos números son amigos si la suma de los divisores");
            System.out.println("propios de uno es igual al otro número.");
            System.out.println("Ejemplo: 220 y 284 son amigos.\n");
            
            Scanner scanner = new Scanner(System.in);
            
            while (true) {
                try {
                    System.out.print("\nIngrese el primer número (0 para salir): ");
                    int num1 = scanner.nextInt();
                    
                    if (num1 == 0) {
                        System.out.println("¡Hasta luego!");
                        break;
                    }
                    
                    System.out.print("Ingrese el segundo número: ");
                    int num2 = scanner.nextInt();
                    
                    // Verificar en el servidor
                    boolean resultado = servicio.sonAmigos(num1, num2);
                    
                    System.out.println("\nResultado: " + num1 + " y " + num2 + 
                                     (resultado ? " SÍ" : " NO") + " son números amigos.");
                    
                } catch (Exception e) {
                    System.out.println("Error en la entrada. Intente nuevamente.");
                    scanner.nextLine(); // Limpiar buffer
                }
            }
            
            scanner.close();
            
        } catch (Exception e) {
            System.err.println("Error en el cliente: " + e.toString());
            e.printStackTrace();
        }
    }
}
