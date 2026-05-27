package ejemplo;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServidorAmigos implements AmigosInterfaz {
    
    @Override
    public boolean sonAmigos(int num1, int num2) throws RemoteException {
        System.out.println("Verificando si " + num1 + " y " + num2 + " son amigos...");
        
        int sumaDivisoresNum1 = calcularSumaDivisores(num1);
        int sumaDivisoresNum2 = calcularSumaDivisores(num2);
        
        return (sumaDivisoresNum1 == num2 && sumaDivisoresNum2 == num1);
    }
    
    private int calcularSumaDivisores(int numero) {
        int suma = 0;
        
        for (int i = 1; i < numero; i++) {
            if (numero % i == 0) {
                suma += i;
            }
        }
        
        return suma;
    }
    
    public static void main(String[] args) {
        try {
            // Crear el objeto remoto
            ServidorAmigos servidor = new ServidorAmigos();
            
            // Exportar el objeto remoto
            AmigosInterfaz stub = (AmigosInterfaz) UnicastRemoteObject.exportObject(servidor, 0);
            
            // Crear el registro RMI en el puerto 1099
            Registry registry = LocateRegistry.createRegistry(1099);
            
            // Registrar el stub en el registro con un nombre
            registry.rebind("ServicioAmigos", stub);
            
            System.out.println("Servidor RMI iniciado. Esperando conexiones...");
            System.out.println("Pares de números amigos conocidos: (220,284), (1184,1210), (2620,2924)");
            
        } catch (Exception e) {
            System.err.println("Error en el servidor: " + e.toString());
            e.printStackTrace();
        }
    }
}
