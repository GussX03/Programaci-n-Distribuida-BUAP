package ejemplo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface EdadInterfaz extends Remote {
    // Método para consultar edad por nombre
    Integer consultarEdad(String nombre) throws RemoteException;
    
    // Método opcional: agregar persona
    boolean agregarPersona(String nombre, int edad) throws RemoteException;
    
    // Método opcional: actualizar edad
    boolean actualizarEdad(String nombre, int nuevaEdad) throws RemoteException;
    
    // Método opcional: eliminar persona
    boolean eliminarPersona(String nombre) throws RemoteException;
}