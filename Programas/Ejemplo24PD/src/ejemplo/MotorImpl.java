package ejemplo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MotorImpl extends UnicastRemoteObject implements MotorRemoto {

    protected MotorImpl() throws RemoteException { super(); }

    @Override
    public <T> T ejecutarTarea(Tarea<T> t) throws RemoteException {
        System.out.println("Ejecutando una tarea enviada por el cliente...");
        return t.ejecutar(); // El servidor ejecuta la lógica definida por el cliente
    }

    public static void main(String[] args) {
        try {
            MotorRemoto motor = new MotorImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("ComputeServer", motor);
            System.out.println("Motor de Cómputo listo para recibir tareas.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}