package ejemplo;

import java.rmi.registry.*;

public class ClienteEcho {
    public static void main(String[] args) {
        try {
            Registry reg = LocateRegistry.getRegistry("localhost", 1099);
            Echo stub = (Echo) reg.lookup("EchoService");
            System.out.println(stub.repetir("hola mundo rmi"));
        } catch (Exception e) { e.printStackTrace(); }
    }
}