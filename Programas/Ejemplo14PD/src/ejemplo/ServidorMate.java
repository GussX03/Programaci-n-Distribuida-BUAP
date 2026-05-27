package ejemplo;

import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;

public class ServidorMate implements Mate {
    public long factorial(int n) {
        long res = 1;
        for (int i = 1; i <= n; i++) res *= i;
        return res;
    }

    public static void main(String[] args) {
        try {
            Mate stub = (Mate) UnicastRemoteObject.exportObject(new ServidorMate(), 0);
            LocateRegistry.createRegistry(1099).rebind("MateService", stub);
            System.out.println("Servidor Matemático listo.");
        } catch (Exception e) { e.printStackTrace(); }
    }
}