package ejemplo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SensorServer extends Remote {
    void registerAdmin(AdminClient admin) throws RemoteException;
    void sendReading(String sensorId, double value) throws RemoteException;
}