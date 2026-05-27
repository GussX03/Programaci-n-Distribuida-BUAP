package ejemplo;

import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;

public class ServidorTemp implements Termometro {
    public double getTemperatura() { return 20.0 + (Math.random() * 10); }

    public static void main(String[] args) {
        try {
            Termometro stub = (Termometro) UnicastRemoteObject.exportObject(new ServidorTemp(), 0);
            LocateRegistry.createRegistry(1099).rebind("TempService", stub);
            System.out.println("Servidor de Temperatura activo.");
        } catch (Exception e) { e.printStackTrace(); }
    }
}