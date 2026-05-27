package ejemplo;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;

public class AppServicioNotificaciones extends UnicastRemoteObject implements ServicioNotificaciones {
    
    public AppServicioNotificaciones() throws RemoteException {
        super();
    }

    @Override
    public synchronized void enviarAlerta(String mensaje) throws RemoteException {
        // Simulación de infraestructura de logs/alertas industriales
        System.out.println("[NOTIFICACIÓN CENTRAL] [" + LocalDateTime.now() + "] ALERTA: " + mensaje);
    }

    public static void main(String[] args) {
        try {
            // Se conecta al registro en el puerto 1099
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            AppServicioNotificaciones servicio = new AppServicioNotificaciones();
            
            // Publica su contrato en el Broker
            registry.rebind("ServicioNotificaciones", servicio);
            System.out.println(">>> AppServicioNotificaciones desplegada y registrada exitosamente.");
        } catch (Exception e) {
            System.err.println("Error al iniciar el Servicio de Notificaciones: " + e.getMessage());
            e.printStackTrace();
        }
    }
}