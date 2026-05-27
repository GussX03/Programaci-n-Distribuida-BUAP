package ejemplo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicioSeguimiento extends Remote {
    // Registra un nuevo paquete y devuelve confirmación
    void registrarPaquete(Paquete p) throws RemoteException;
    
    // Busca un paquete por ID
    Paquete consultarEstado(String id) throws RemoteException;
    
    // Actualiza el estado (ej. "En camino", "Entregado")
    boolean actualizarEstado(String id, String nuevoEstado) throws RemoteException;
}