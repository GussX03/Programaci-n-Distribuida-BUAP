package ejemplo;

import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ServidorMaquinaExpendedora {

    // Mapa que almacena los productos (código -> nombre y precio)
    private static Map<String, Producto> productos = new HashMap<>();
    
    static {
        // Inicializamos algunos productos de ejemplo
        productos.put("A01", new Producto("Coca-Cola", 15.0));
        productos.put("A02", new Producto("Pepsi", 14.0));
        productos.put("A03", new Producto("Agua", 10.0));
        productos.put("B01", new Producto("Papas fritas", 12.0));
        productos.put("B02", new Producto("Galletas", 8.0));
        productos.put("C01", new Producto("Chocolate", 18.0));
        productos.put("C02", new Producto("Gomitas", 6.0));
    }

    public static void main(String args[]) {

        try {
            DatagramSocket socketUDP = new DatagramSocket(6789);
            byte[] bufer = new byte[1024];
            
            System.out.println("=== MÁQUINA EXPENDEDORA UDP ===");
            System.out.println("Servidor iniciado en el puerto 6789");
            System.out.println("Productos disponibles:");
            mostrarProductos();
            System.out.println("================================");
            
            while (true) {
                // Construimos el DatagramPacket para recibir peticiones
                DatagramPacket peticion = new DatagramPacket(bufer, bufer.length);
                
                // Leemos una petición del DatagramSocket
                socketUDP.receive(peticion);
                
                // Obtenemos los datos del cliente
                String datosCliente = new String(peticion.getData(), 0, peticion.getLength());
                System.out.println("\n--- Nueva solicitud recibida ---");
                System.out.println("Cliente: " + peticion.getAddress() + ":" + peticion.getPort());
                System.out.println("Datos recibidos: " + datosCliente);
                
                // Procesamos la solicitud
                String respuesta = procesarSolicitud(datosCliente);
                
                // Enviamos la respuesta al cliente
                byte[] respuestaBytes = respuesta.getBytes();
                DatagramPacket respuestaPacket = new DatagramPacket(
                    respuestaBytes, respuestaBytes.length,
                    peticion.getAddress(), peticion.getPort()
                );
                
                socketUDP.send(respuestaPacket);
                System.out.println("Respuesta enviada: " + respuesta);
            }
            
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }
    
    private static String procesarSolicitud(String datos) {
        try {
            // Formato esperado: "codigo,cantidadDinero"
            String[] partes = datos.split(",");
            
            if (partes.length != 2) {
                return "ERROR: Formato incorrecto. Use: codigo,cantidad";
            }
            
            String codigo = partes[0].trim();
            double dinero = Double.parseDouble(partes[1].trim());
            
            // Verificamos si el producto existe
            if (!productos.containsKey(codigo)) {
                return "producto no entregado - Código no existe: " + codigo;
            }
            
            Producto producto = productos.get(codigo);
            
            // Verificamos si el dinero alcanza
            if (dinero >= producto.getPrecio()) {
                double cambio = dinero - producto.getPrecio();
                return String.format("producto entregado - %s ($%.2f) - Su cambio: $%.2f", 
                                   producto.getNombre(), producto.getPrecio(), cambio);
            } else {
                return String.format("producto no entregado - Dinero insuficiente para %s ($%.2f). Faltan: $%.2f", 
                                   producto.getNombre(), producto.getPrecio(), 
                                   producto.getPrecio() - dinero);
            }
            
        } catch (NumberFormatException e) {
            return "ERROR: La cantidad de dinero debe ser un número válido";
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }
    
    private static void mostrarProductos() {
        System.out.println("Código | Producto      | Precio");
        System.out.println("-------|---------------|-------");
        for (Map.Entry<String, Producto> entry : productos.entrySet()) {
            System.out.printf("%-6s | %-13s | $%.2f%n", 
                            entry.getKey(), 
                            entry.getValue().getNombre(),
                            entry.getValue().getPrecio());
        }
    }
}

// Clase para representar un producto
class Producto {
    private String nombre;
    private double precio;
    
    public Producto(String nombre, double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public double getPrecio() {
        return precio;
    }
}