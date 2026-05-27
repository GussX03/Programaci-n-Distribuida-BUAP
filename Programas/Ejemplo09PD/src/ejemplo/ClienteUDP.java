package ejemplo;

import java.net.*;
import java.io.*;

public class ClienteUDP {

  public static void main(String args[]) {

    try {
      // Verificamos que se proporcionen los argumentos necesarios
      if (args.length < 2) {
        System.out.println("Uso: java ClienteUDP <mensaje> <servidor>");
        return;
      }
      
      DatagramSocket socketUDP = new DatagramSocket();
      byte[] mensaje = args[0].getBytes();
      InetAddress hostServidor = InetAddress.getByName(args[1]);
      int puertoServidor = 6789;

      // Construimos un datagrama para enviar el mensaje al servidor
      DatagramPacket peticion =
        new DatagramPacket(mensaje, args[0].length(), hostServidor,
                           puertoServidor);

      // Enviamos el datagrama
      socketUDP.send(peticion);
      System.out.println("Petición enviada al servidor");

      // Configuramos el timeout para la recepción (5000 ms)
      socketUDP.setSoTimeout(5000);

      // Construimos el DatagramPacket que contendrá la respuesta
      byte[] bufer = new byte[1000];
      DatagramPacket respuesta =
        new DatagramPacket(bufer, bufer.length);
      
      try {
        // Intentamos recibir la respuesta con timeout
        socketUDP.receive(respuesta);
        
        // Enviamos la respuesta del servidor a la salida estandar
        String fechaHora = new String(respuesta.getData(), 0, respuesta.getLength());
        System.out.println("Fecha y hora recibida: " + fechaHora);
        
      } catch (SocketTimeoutException e) {
        // Si pasan 5000 ms sin recibir respuesta, mostramos mensaje de error
        System.out.println("ERROR: Tiempo de espera agotado (5000 ms). No se recibió respuesta del servidor.");
      }

      // Cerramos el socket
      socketUDP.close();

    } catch (SocketException e) {
      System.out.println("Socket: " + e.getMessage());
    } catch (IOException e) {
      System.out.println("IO: " + e.getMessage());
    }
  }
}