package ejemplo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ReservaImpl extends UnicastRemoteObject implements ReservaInterface {
    private List<Vuelo> listaVuelos;

    protected ReservaImpl() throws RemoteException {
        listaVuelos = new ArrayList<>();
        listaVuelos.add(new Vuelo("AR123", 2)); // Solo 2 asientos para probar concurrencia
        listaVuelos.add(new Vuelo("IB456", 50));
    }

    @Override
    public List<Vuelo> consultarVuelos() throws RemoteException {
        return listaVuelos;
    }

    @Override
    public synchronized String reservarAsiento(String codigoVuelo, String pasajero) throws RemoteException {
        for (Vuelo v : listaVuelos) {
            if (v.getCodigo().equals(codigoVuelo)) {
                if (v.getAsientosLibres() > 0) {
                    // Simulación de retraso de red para probar concurrencia
                    try { Thread.sleep(500); } catch (InterruptedException e) {}
                    
                    v.añadirPasajero(pasajero);
                    System.out.println("Reserva exitosa: " + codigoVuelo + " para " + pasajero);
                    return "CONFIRMADO-ID-" + (int)(Math.random()*1000);
                } else {
                    return "ERROR: Vuelo lleno.";
                }
            }
        }
        return "ERROR: Vuelo no encontrado.";
    }
}