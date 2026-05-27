package ejemplo;

import java.rmi.registry.*;

public class ClienteMate {
    public static void main(String[] args) {
        try {
            Mate stub = (Mate) LocateRegistry.getRegistry("localhost", 1099).lookup("MateService");
            System.out.println("Factorial de 5: " + stub.factorial(5));
        } catch (Exception e) { e.printStackTrace(); }
    }
}