package ejemplo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NotificadorInterface extends Remote {
    void registrarCliente(String nombre) throws RemoteException;
    void enviarMensaje(String msg) throws RemoteException;
}