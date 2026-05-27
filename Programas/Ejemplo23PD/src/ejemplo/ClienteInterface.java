package ejemplo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClienteInterface extends Remote {
    // El servidor llama a este método para entregar un mensaje
    void recibirMensaje(String remitente, String mensaje) throws RemoteException;
}