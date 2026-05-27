package ejemplo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicioNotificaciones extends Remote {
    void enviarAlerta(String mensaje) throws RemoteException;
}