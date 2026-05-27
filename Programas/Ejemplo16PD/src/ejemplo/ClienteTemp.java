package ejemplo;

import java.rmi.registry.*;

public class ClienteTemp {
    public static void main(String[] args) {
        try {
            Termometro stub = (Termometro) LocateRegistry.getRegistry("localhost", 1099).lookup("TempService");
            System.out.printf("Temperatura actual: %.2f °C\n", stub.getTemperatura());
        } catch (Exception e) { e.printStackTrace(); }
    }
}