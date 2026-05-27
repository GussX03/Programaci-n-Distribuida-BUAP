package ejemplo;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class AppClienteIndustrial {

    public static void main(String[] args) {
        try {
            // 1. Conectarse al Broker remoto (Registry)
            System.out.println("[CLIENTE] Conectando al registro de servicios...");
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);

            // 2. Buscar el servicio final que requiere consumir mediante su interfaz (Contrato)
            ServicioOrquestadorPedidos orquestador = (ServicioOrquestadorPedidos) registry.lookup("ServicioOrquestadorPedidos");
            System.out.println("[CLIENTE] Servicio orquestador localizado en el broker.\n");

            // 3. Ejecutar peticiones remotas de prueba
            System.out.println("[CLIENTE] Enviando Pedido 1...");
            String resultado1 = orquestador.procesarOrdenIndustrial("PLC-SIEMENS-S7", 3);
            System.out.println("[RESPUESTA SISTEMA]: " + resultado1);
            
            System.out.println("\n[CLIENTE] Enviando Pedido 2 (Forzando exceso de stock)...");
            String resultado2 = orquestador.procesarOrdenIndustrial("MOTOR-TRIFASICO-2HP", 10);
            System.out.println("[RESPUESTA SISTEMA]: " + resultado2);

        } catch (Exception e) {
            System.err.println("[CLIENTE ERROR] Excepción en la ejecución del cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }
}