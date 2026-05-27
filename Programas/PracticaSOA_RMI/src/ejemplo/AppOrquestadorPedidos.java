package ejemplo;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;

public class AppOrquestadorPedidos extends UnicastRemoteObject implements ServicioOrquestadorPedidos {
    
    private Registry registry;

    public AppOrquestadorPedidos() throws RemoteException {
        super();
        try {
            // Localiza el Broker para buscar los servicios que va a orquestar
            this.registry = LocateRegistry.getRegistry("localhost", 1099);
        } catch (Exception e) {
            System.err.println("Orquestador no pudo conectar con el registro RMI.");
        }
    }

    @Override
    public synchronized String procesarOrdenIndustrial(String idArticulo, int cantidad) throws RemoteException {
        String idOrden = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        System.out.println("\n[ORQUESTADOR] Iniciando procesamiento de orden #" + idOrden);

        try {
            // 1. Descubrimiento dinámico de los servicios requeridos en el Broker
            ServicioInventario inventario = (ServicioInventario) registry.lookup("ServicioInventario");
            ServicioNotificaciones notificaciones = (ServicioNotificaciones) registry.lookup("ServicioNotificaciones");

            // 2. Orquestación del flujo lógico
            boolean stockAprobado = inventario.verificarYReducirStock(idArticulo, cantidad);

            if (stockAprobado) {
                String msgExito = "Orden #" + idOrden + " PROCESADA. Despachados " + cantidad + " de " + idArticulo;
                // Invoca al servicio técnico de notificaciones
                notificaciones.enviarAlerta(msgExito);
                return "COMPLETA - " + msgExito;
            } else {
                String msgFallo = "Orden #" + idOrden + " RECHAZADA. Stock insuficiente de " + idArticulo;
                notificaciones.enviarAlerta(msgFallo);
                return "RECHAZADA - Inventario crítico.";
            }

        } catch (Exception e) {
            System.err.println("[ORQUESTADOR ERROR] Falla en la comunicación entre servicios: " + e.getMessage());
            throw new RemoteException("Falla interna de orquestación SOA: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            AppOrquestadorPedidos orquestador = new AppOrquestadorPedidos();
            
            registry.rebind("ServicioOrquestadorPedidos", orquestador);
            System.out.println(">>> AppOrquestadorPedidos (Orquestador SOA) activo y registrado.");
        } catch (Exception e) {
            System.err.println("Error al iniciar el Orquestador: " + e.getMessage());
            e.printStackTrace();
        }
    }
}