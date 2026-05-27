package ejemplo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class SeguimientoImpl extends UnicastRemoteObject implements ServicioSeguimiento {
    private Map<String, Paquete> baseDatos;

    protected SeguimientoImpl() throws RemoteException {
        super();
        baseDatos = new HashMap<>();
    }

    @Override
    public synchronized void registrarPaquete(Paquete p) throws RemoteException {
        baseDatos.put("PK-" + (baseDatos.size() + 1), p);
        System.out.println("Paquete registrado: " + p);
    }

    @Override
    public Paquete consultarEstado(String id) throws RemoteException {
        return baseDatos.get(id);
    }

    @Override
    public synchronized boolean actualizarEstado(String id, String nuevoEstado) throws RemoteException {
        if (baseDatos.containsKey(id)) {
            baseDatos.get(id).setEstado(nuevoEstado);
            return true;
        }
        return false;
    }
}