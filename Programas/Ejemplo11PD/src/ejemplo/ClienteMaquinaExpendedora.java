package ejemplo;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ClienteMaquinaExpendedora {

    public static void main(String args[]) {
        
        try {
            // Verificamos que se proporcionen los argumentos necesarios
            if (args.length < 2) {
                System.out.println("Uso: java ClienteMaquinaExpendedora <servidor> <puerto>");
                System.out.println("Ejemplo: java ClienteMaquinaExpendedora localhost 6789");
                return;
            }
            
            String servidor = args[0];
            int puerto = Integer.parseInt(args[1]);
            
            DatagramSocket socketUDP = new DatagramSocket();
            InetAddress hostServidor = InetAddress.getByName(servidor);
            
            // Configuramos timeout de 10 segundos
            socketUDP.setSoTimeout(10000);
            
            Scanner scanner = new Scanner(System.in);
            
            System.out.println("=== MÁQUINA EXPENDEDORA CLIENTE ===");
            System.out.println("Conectando al servidor: " + servidor + ":" + puerto);
            System.out.println("====================================");
            
            boolean continuar = true;
            
            while (continuar) {
                System.out.println("\n--- Nueva compra ---");
                
                // Solicitamos el código del producto
                System.out.print("Ingrese el código del producto (o 'salir' para terminar): ");
                String codigo = scanner.nextLine().trim();
                
                if (codigo.equalsIgnoreCase("salir")) {
                    continuar = false;
                    System.out.println("Gracias por usar la máquina expendedora. ¡Hasta luego!");
                    break;
                }
                
                // Solicitamos la cantidad de dinero
                System.out.print("Ingrese la cantidad de dinero (ejemplo: 15.50): ");
                String dineroStr = scanner.nextLine().trim();
                
                try {
                    double dinero = Double.parseDouble(dineroStr);
                    
                    // Construimos el mensaje para el servidor
                    String mensaje = codigo + "," + dinero;
                    byte[] mensajeBytes = mensaje.getBytes();
                    
                    // Enviamos la petición al servidor
                    DatagramPacket peticion = new DatagramPacket(
                        mensajeBytes, mensajeBytes.length,
                        hostServidor, puerto
                    );
                    
                    socketUDP.send(peticion);
                    System.out.println("Solicitud enviada al servidor...");
                    
                    // Esperamos la respuesta
                    byte[] bufer = new byte[1024];
                    DatagramPacket respuesta = new DatagramPacket(bufer, bufer.length);
                    
                    socketUDP.receive(respuesta);
                    
                    // Mostramos la respuesta
                    String resultado = new String(respuesta.getData(), 0, respuesta.getLength());
                    System.out.println("\n*** RESULTADO: " + resultado + " ***");
                    
                } catch (NumberFormatException e) {
                    System.out.println("Error: La cantidad de dinero debe ser un número válido.");
                } catch (SocketTimeoutException e) {
                    System.out.println("Error: Tiempo de espera agotado. El servidor no respondió.");
                }
            }
            
            scanner.close();
            socketUDP.close();
            
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error: El puerto debe ser un número válido.");
        }
    }
}