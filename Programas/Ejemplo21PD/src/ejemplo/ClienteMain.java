package ejemplo;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClienteMain {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            ServicioSeguimiento servicio = (ServicioSeguimiento) registry.lookup("CourierService");

            // 1. Registrar un paquete
            Paquete miPaquete = new Paquete("001", "Ciudad de México");
            servicio.registrarPaquete(miPaquete);

            // 2. Consultar y Actualizar
            System.out.println("Consultando PK-1...");
            Paquete p = servicio.consultarEstado("PK-1");
            
            if (p != null) {
                System.out.println("Encontrado: " + p);
                servicio.actualizarEstado("PK-1", "En tránsito");
                System.out.println("Nuevo estado: " + servicio.consultarEstado("PK-1"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}