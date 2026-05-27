package ejemplo;

import java.rmi.registry.LocateRegistry;

public class UpperCliente {
    public static void main(String[] args) {
        try {
            UpperInterface stub = (UpperInterface) LocateRegistry.getRegistry("localhost").lookup("UpperService");
            System.out.println("Resultado: " + stub.transformar("hola mundo rmi"));
        } catch (Exception e) { e.printStackTrace(); }
    }
}
