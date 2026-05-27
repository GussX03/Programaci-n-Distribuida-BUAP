package ejemplo;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class ClienteEdad {
    
    private static EdadInterfaz servicio;
    private static Scanner scanner;
    
    public static void main(String[] args) {
        String host = "localhost";
        
        try {
            // Conectar al registro RMI
            Registry registry = LocateRegistry.getRegistry(host, 1099);
            servicio = (EdadInterfaz) registry.lookup("ServicioEdad");
            
            scanner = new Scanner(System.in);
            
            System.out.println("=== Sistema de Consulta de Edades ===");
            System.out.println("Conectado al servidor RMI\n");
            
            mostrarMenu();
            
        } catch (Exception e) {
            System.err.println("Error de conexión: " + e.getMessage());
            System.err.println("Asegúrese de que el servidor esté ejecutándose.");
        }
    }
    
    private static void mostrarMenu() {
        int opcion;
        
        do {
            System.out.println("\n=== MENÚ PRINCIPAL ===");
            System.out.println("1. Consultar edad por nombre");
            System.out.println("2. Agregar nueva persona");
            System.out.println("3. Actualizar edad");
            System.out.println("4. Eliminar persona");
            System.out.println("5. Salir");
            System.out.print("\nSeleccione una opción: ");
            
            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer
            
            switch (opcion) {
                case 1:
                    consultarEdad();
                    break;
                case 2:
                    agregarPersona();
                    break;
                case 3:
                    actualizarEdad();
                    break;
                case 4:
                    eliminarPersona();
                    break;
                case 5:
                    System.out.println("¡Gracias por usar el sistema!");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        } while (opcion != 5);
        
        scanner.close();
    }
    
    private static void consultarEdad() {
        System.out.print("\nIngrese el nombre completo: ");
        String nombre = scanner.nextLine();
        
        try {
            Integer edad = servicio.consultarEdad(nombre);
            
            if (edad != null) {
                System.out.println("\n✓ " + nombre + " tiene " + edad + " años.");
            } else {
                System.out.println("\n✗ No se encontró información para: " + nombre);
                System.out.println("  Use la opción 2 para agregarlo.");
            }
        } catch (Exception e) {
            System.err.println("Error en la consulta: " + e.getMessage());
        }
    }
    
    private static void agregarPersona() {
        System.out.print("\nIngrese el nombre completo: ");
        String nombre = scanner.nextLine();
        
        System.out.print("Ingrese la edad: ");
        int edad = scanner.nextInt();
        scanner.nextLine();
        
        try {
            boolean exito = servicio.agregarPersona(nombre, edad);
            
            if (exito) {
                System.out.println("\n✓ Persona agregada exitosamente!");
            } else {
                System.out.println("\n✗ No se pudo agregar. Verifique:");
                System.out.println("  - La persona ya existe");
                System.out.println("  - Edad válida (0-150)");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private static void actualizarEdad() {
        System.out.print("\nIngrese el nombre completo: ");
        String nombre = scanner.nextLine();
        
        System.out.print("Ingrese la nueva edad: ");
        int nuevaEdad = scanner.nextInt();
        scanner.nextLine();
        
        try {
            boolean exito = servicio.actualizarEdad(nombre, nuevaEdad);
            
            if (exito) {
                System.out.println("\n✓ Edad actualizada correctamente!");
            } else {
                System.out.println("\n✗ No se encontró la persona.");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private static void eliminarPersona() {
        System.out.print("\nIngrese el nombre completo: ");
        String nombre = scanner.nextLine();
        
        System.out.print("¿Está seguro? (s/n): ");
        String confirmacion = scanner.nextLine();
        
        if (!confirmacion.equalsIgnoreCase("s")) {
            System.out.println("Operación cancelada.");
            return;
        }
        
        try {
            boolean exito = servicio.eliminarPersona(nombre);
            
            if (exito) {
                System.out.println("\n✓ Persona eliminada del sistema.");
            } else {
                System.out.println("\n✗ No se encontró la persona.");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}