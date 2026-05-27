package ejemplo;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class SensorServerImpl extends UnicastRemoteObject implements SensorServer {
    private final List<AdminClient> admins;

    protected SensorServerImpl() throws RemoteException {
        admins = new ArrayList<>();
    }

    @Override
    public synchronized void registerAdmin(AdminClient admin) throws RemoteException {
        admins.add(admin);
        System.out.println("Administrador registrado.");
    }

    @Override
    public synchronized void sendReading(String sensorId, double value) throws RemoteException {
        System.out.println("Lectura recibida de " + sensorId + ": " + value);

        // Procesamiento concurrente
        new Thread(() -> {
            try {
                if (value > 50.0) { // umbral crítico
                    String alert = "ALERTA: Sensor " + sensorId + " detectó valor crítico: " + value;
                    notifyAdmins(alert);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void notifyAdmins(String alert) throws RemoteException {
        for (AdminClient admin : admins) {
            admin.notifyAlert(alert);
        }
    }
}