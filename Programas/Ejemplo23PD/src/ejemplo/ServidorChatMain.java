package ejemplo;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServidorChatMain {
    public static void main(String[] args) {
        try {
            ChatImpl chat = new ChatImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("ChatService", chat);
            System.out.println("Servidor de Chat iniciado...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}