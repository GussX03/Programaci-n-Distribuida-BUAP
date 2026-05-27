package ejemplo;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class AgenciaViajes {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            ReservaInterface sistema = (ReservaInterface) registry.lookup("SistemaVuelos");

            // 1. Ver disponibilidad
            System.out.println("--- Vuelos Disponibles ---");
            List<Vuelo> disponibles = sistema.consultarVuelos();
            disponibles.forEach(System.out::println);

            // 2. Intentar reservar
            String pasajero = "Juan Perez";
            String codigo = "AR123";
            
            System.out.println("\nIntentando reservar " + codigo + " para " + pasajero + "...");
            String resultado = sistema.reservarAsiento(codigo, pasajero);
            
            System.out.println("Resultado del Servidor: " + resultado);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}