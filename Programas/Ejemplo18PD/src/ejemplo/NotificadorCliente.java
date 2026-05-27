package ejemplo;

import java.rmi.registry.LocateRegistry;

public class NotificadorCliente {
    public static void main(String[] args) {
        try {
            NotificadorInterface service = (NotificadorInterface) LocateRegistry.getRegistry("localhost").lookup("Notificador");
            service.registrarCliente("Admin_PC");
            service.enviarMensaje("Sistema reiniciando en 5 min");
        } catch (Exception e) { e.printStackTrace(); }
    }
}