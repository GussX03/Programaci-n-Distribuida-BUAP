package ejemplo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatInterface extends Remote {
    // Permite a un cliente registrarse para recibir notificaciones
    void registrarCliente(ClienteInterface cliente, String nombre) throws RemoteException;
    
    // Envía un mensaje a todos los conectados
    void difundirMensaje(String nombre, String mensaje) throws RemoteException;
}