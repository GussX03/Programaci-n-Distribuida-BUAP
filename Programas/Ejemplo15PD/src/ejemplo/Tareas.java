package ejemplo;

import java.rmi.*;
import java.util.List;

public interface Tareas extends Remote {
    void añadirTarea(String t) throws RemoteException;
    List<String> verTareas() throws RemoteException;
}