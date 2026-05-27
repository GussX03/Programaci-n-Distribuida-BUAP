package ejemplo;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ReservaInterface extends Remote {
    List<Vuelo> consultarVuelos() throws RemoteException;
    
    // Devuelve un código de confirmación o un error
    String reservarAsiento(String codigoVuelo, String nombrePasajero) throws RemoteException;
}