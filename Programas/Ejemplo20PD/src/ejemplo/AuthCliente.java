package ejemplo;

import java.rmi.registry.LocateRegistry;

public class AuthCliente {
    public static void main(String[] args) {
        try {
            AuthInterface auth = (AuthInterface) LocateRegistry.getRegistry("localhost").lookup("Auth");
            boolean exito = auth.validar("root", "secret123");
            System.out.println("¿Acceso permitido?: " + (exito ? "SÍ" : "NO"));
        } catch (Exception e) { e.printStackTrace(); }
    }
}