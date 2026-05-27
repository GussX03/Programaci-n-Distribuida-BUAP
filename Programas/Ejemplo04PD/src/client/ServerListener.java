package client;

import common.models.Notification;
import common.protocol.Response;
import common.protocol.ResponseType;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.util.concurrent.BlockingQueue;

// Este hilo escucha constantemente el servidor en busca de notificaciones push
public class ServerListener extends Thread {
    private ObjectInputStream in;
    private NotificationClient client; // Referencia al cliente para manejar la notificación
    private BlockingQueue<Response> responseQueue;

    public ServerListener(ObjectInputStream in, NotificationClient client, BlockingQueue<Response> responseQueue) {
        this.in = in;
        this.client = client;
        this.responseQueue = responseQueue;
        this.setDaemon(true); // Se detendrá cuando el hilo principal del cliente finalice
    }

    @Override
    public void run() {
        try {
            Response response;
            while (!isInterrupted() && (response = (Response) in.readObject())!= null) {
                if (response.getType() == ResponseType.NOTIFICATION) {
                    if (response.getPayload() instanceof Notification) {
                        client.handleIncomingNotification((Notification) response.getPayload());
                    }
                } else {
                    responseQueue.offer(response);
                }
            }
        } catch (EOFException e) {
            System.out.println("El servidor cerró la conexión.");
        } catch (Exception e) {
            if (!isInterrupted()) { // Solo reportar si no fue una interrupción intencional
                System.err.println("Error en el hilo de escucha del servidor: " + e.getMessage());
                // e.printStackTrace();
            }
        } finally {
            System.out.println("Hilo de escucha del servidor terminado.");
        }
    }
}
