package ejemplo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class ChatImpl extends UnicastRemoteObject implements ChatInterface {
    // Mapa de Clientes conectados (Nombre -> Referencia Remota)
    private Map<String, ClienteInterface> clientesConectados;

    protected ChatImpl() throws RemoteException {
        clientesConectados = new HashMap<>();
    }

    @Override
    public synchronized void registrarCliente(ClienteInterface cliente, String nombre) throws RemoteException {
        clientesConectados.put(nombre, cliente);
        System.out.println("DEBUG: " + nombre + " se ha unido al chat.");
    }

    @Override
    public synchronized void difundirMensaje(String nombre, String mensaje) throws RemoteException {
        System.out.println(nombre + " dice: " + mensaje);
        
        // Iteramos sobre las referencias remotas de los clientes para "empujar" el mensaje
        for (Map.Entry<String, ClienteInterface> entry : clientesConectados.entrySet()) {
            try {
                entry.getValue().recibirMensaje(nombre, mensaje);
            } catch (RemoteException e) {
                System.out.println("No se pudo contactar a " + entry.getKey() + ". Eliminando...");
                // En un sistema real, aquí eliminaríamos al cliente desconectado
            }
        }
    }
}
