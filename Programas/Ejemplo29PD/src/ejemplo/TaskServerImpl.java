package ejemplo;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.concurrent.PriorityBlockingQueue;

public class TaskServerImpl extends UnicastRemoteObject implements TaskServer {
    private final PriorityBlockingQueue<Task> queue;

    protected TaskServerImpl() throws RemoteException {
        queue = new PriorityBlockingQueue<>();
        startProcessor();
    }

    @Override
    public synchronized void registerClient(TaskClient client) throws RemoteException {
        System.out.println("Cliente registrado.");
    }

    @Override
    public synchronized void submitTask(String description, int priority, TaskClient client) throws RemoteException {
        Task task = new Task(description, priority, client);
        queue.add(task);
        System.out.println("Tarea recibida: " + description + " con prioridad " + priority);
    }

    private void startProcessor() {
        new Thread(() -> {
            while (true) {
                try {
                    Task task = queue.take();
                    Thread.sleep(2000); // simula procesamiento
                    if (task.getClient() != null) {
                        task.getClient().notifyResult("Tarea '" + task.getDescription() + "' completada.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
