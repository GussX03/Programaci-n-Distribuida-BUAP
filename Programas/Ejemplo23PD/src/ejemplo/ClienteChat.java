package ejemplo;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class ClienteChat extends UnicastRemoteObject implements ClienteInterface {
    
    protected ClienteChat() throws RemoteException { super(); }

    @Override
    public void recibirMensaje(String remitente, String mensaje) throws RemoteException {
        System.out.println("\n[" + remitente + "]: " + mensaje);
        System.out.print("Tú: "); // Mantener el prompt visual
    }

    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.print("Ingresa tu nombre: ");
            String nombre = sc.nextLine();

            // 1. Localizar el servidor
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            ChatInterface servidor = (ChatInterface) registry.lookup("ChatService");

            // 2. Exportar este cliente como objeto remoto para recibir callbacks
            ClienteChat miInstancia = new ClienteChat();
            servidor.registrarCliente(miInstancia, nombre);

            // 3. Bucle para enviar mensajes
            System.out.println("Conectado. Escribe tus mensajes:");
            while (true) {
                String msg = sc.nextLine();
                servidor.difundirMensaje(nombre, msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}