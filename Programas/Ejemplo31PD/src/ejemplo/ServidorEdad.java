package ejemplo;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ServidorEdad implements EdadInterfaz {
    
    // HashMap como estructura de almacenamiento principal
    private final Map<String, Integer> baseDatosEdades;
    
    // Lock para manejar concurrencia (permite múltiples lecturas, escritura exclusiva)
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    
    public ServidorEdad() {
        // Inicializar HashMap con capacidad inicial y factor de carga
        this.baseDatosEdades = new HashMap<>(100, 0.75f);
        cargarDatosIniciales();
    }
    
    private void cargarDatosIniciales() {
        // Datos de ejemplo
        baseDatosEdades.put("Juan Perez", 25);
        baseDatosEdades.put("Maria Garcia", 30);
        baseDatosEdades.put("Carlos Lopez", 28);
        baseDatosEdades.put("Ana Martinez", 22);
        baseDatosEdades.put("Luis Rodriguez", 35);
        baseDatosEdades.put("Elena Sanchez", 27);
        baseDatosEdades.put("Pedro Gomez", 40);
        baseDatosEdades.put("Laura Diaz", 33);
    }
    
    @Override
    public Integer consultarEdad(String nombre) throws RemoteException {
        System.out.println("Consulta recibida para: " + nombre);
        
        // Validación de entrada
        if (nombre == null || nombre.trim().isEmpty()) {
            System.out.println("Error: Nombre inválido");
            return null;
        }
        
        // Normalizar nombre (primer letra mayúscula, resto minúsculas)
        nombre = normalizarNombre(nombre);
        
        // Bloqueo de lectura para permitir múltiples consultas concurrentes
        lock.readLock().lock();
        try {
            Integer edad = baseDatosEdades.get(nombre);
            
            if (edad != null) {
                System.out.println("Encontrado: " + nombre + " -> " + edad + " años");
                return edad;
            } else {
                System.out.println("No encontrado: " + nombre);
                return null;
            }
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public boolean agregarPersona(String nombre, int edad) throws RemoteException {
        System.out.println("Intento de agregar: " + nombre + ", " + edad + " años");
        
        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }
        
        if (edad < 0 || edad > 150) {
            System.out.println("Edad inválida: " + edad);
            return false;
        }
        
        nombre = normalizarNombre(nombre);
        
        // Bloqueo de escritura (exclusivo)
        lock.writeLock().lock();
        try {
            if (baseDatosEdades.containsKey(nombre)) {
                System.out.println("La persona ya existe: " + nombre);
                return false;
            }
            
            baseDatosEdades.put(nombre, edad);
            System.out.println("Persona agregada exitosamente. Total: " + baseDatosEdades.size());
            return true;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public boolean actualizarEdad(String nombre, int nuevaEdad) throws RemoteException {
        System.out.println("Intento de actualizar: " + nombre + " -> " + nuevaEdad + " años");
        
        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }
        
        if (nuevaEdad < 0 || nuevaEdad > 150) {
            return false;
        }
        
        nombre = normalizarNombre(nombre);
        
        lock.writeLock().lock();
        try {
            if (!baseDatosEdades.containsKey(nombre)) {
                System.out.println("Persona no encontrada: " + nombre);
                return false;
            }
            
            baseDatosEdades.put(nombre, nuevaEdad);
            System.out.println("Edad actualizada exitosamente");
            return true;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public boolean eliminarPersona(String nombre) throws RemoteException {
        System.out.println("Intento de eliminar: " + nombre);
        
        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }
        
        nombre = normalizarNombre(nombre);
        
        lock.writeLock().lock();
        try {
            Integer eliminado = baseDatosEdades.remove(nombre);
            if (eliminado != null) {
                System.out.println("Persona eliminada exitosamente. Total: " + baseDatosEdades.size());
                return true;
            } else {
                System.out.println("Persona no encontrada: " + nombre);
                return false;
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    private String normalizarNombre(String nombre) {
        // Normalizar: trim y capitalizar cada palabra
        String[] palabras = nombre.trim().toLowerCase().split(" ");
        StringBuilder normalizado = new StringBuilder();
        
        for (int i = 0; i < palabras.length; i++) {
            if (palabras[i].length() > 0) {
                normalizado.append(Character.toUpperCase(palabras[i].charAt(0)))
                          .append(palabras[i].substring(1));
                if (i < palabras.length - 1) {
                    normalizado.append(" ");
                }
            }
        }
        
        return normalizado.toString();
    }
    
    public void mostrarEstadisticas() {
        System.out.println("\n=== Estadísticas del Servidor ===");
        System.out.println("Total de personas registradas: " + baseDatosEdades.size());
        System.out.println("Capacidad actual del HashMap: " + baseDatosEdades.size());
        lock.readLock().lock();
        try {
            System.out.println("\nLista completa:");
            for (Map.Entry<String, Integer> entry : baseDatosEdades.entrySet()) {
                System.out.println("  " + entry.getKey() + ": " + entry.getValue() + " años");
            }
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public static void main(String[] args) {
        try {
            // Crear el servidor
            ServidorEdad servidor = new ServidorEdad();
            
            // Exportar el objeto remoto
            EdadInterfaz stub = (EdadInterfaz) UnicastRemoteObject.exportObject(servidor, 0);
            
            // Crear registro RMI en puerto 1099
            Registry registry = LocateRegistry.createRegistry(1099);
            
            // Registrar el stub
            registry.rebind("ServicioEdad", stub);
            
            System.out.println("=== SERVIDOR RMI INICIADO ===");
            System.out.println("Estructura de almacenamiento: HashMap");
            System.out.println("Complejidad de búsqueda: O(1)");
            System.out.println("Puerto RMI: 1099");
            
            servidor.mostrarEstadisticas();
            
            System.out.println("\nServidor listo para recibir consultas...");
            
        } catch (Exception e) {
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}