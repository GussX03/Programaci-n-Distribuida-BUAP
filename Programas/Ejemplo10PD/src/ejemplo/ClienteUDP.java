package ejemplo;

import java.net.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;
import java.time.LocalTime;
import java.time.LocalDate;

public class ClienteUDP {

  public static void main(String args[]) {

    try {
      // Verificamos que se proporcionen los argumentos necesarios
      if (args.length < 2) {
        System.out.println("Uso: java ClienteUDP <mensaje> <servidor>");
        return;
      }
      
      // Obtenemos la hora local antes de enviar la petición
      LocalDateTime horaLocalAntes = LocalDateTime.now();
      
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
        
        // Obtenemos la hora local después de recibir la respuesta
        LocalDateTime horaLocalDespues = LocalDateTime.now();
        
        // Parseamos la fecha y hora recibida del servidor
        String fechaHoraRecibida = new String(respuesta.getData(), 0, respuesta.getLength());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime horaRemota = LocalDateTime.parse(fechaHoraRecibida, formatter);
        
        // Calculamos la diferencia entre la hora remota y la hora local
        // Para una comparación más precisa, usamos el promedio de la hora local antes y después
        LocalDateTime horaLocalPromedio = horaLocalAntes.plusNanos(
            Duration.between(horaLocalAntes, horaLocalDespues).toNanos() / 2);
        
        // Calculamos la diferencia en milisegundos
        long diferenciaMs = Duration.between(horaRemota, horaLocalPromedio).toMillis();
        
        // Calculamos la diferencia en segundos (con decimales)
        double diferenciaSegundos = diferenciaMs / 1000.0;
        
        // Mostramos la información
        System.out.println("\n=== INFORMACIÓN DE TIEMPO ===");
        System.out.println("Hora remota (servidor): " + fechaHoraRecibida);
        System.out.println("Hora local (cliente):   " + 
                          horaLocalPromedio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        
        // Mostramos la diferencia con formato adecuado
        if (diferenciaMs < 0) {
            System.out.println("Diferencia: El servidor está " + 
                              String.format("%.3f", Math.abs(diferenciaSegundos)) + 
                              " segundos adelantado respecto al cliente");
        } else if (diferenciaMs > 0) {
            System.out.println("Diferencia: El servidor está " + 
                              String.format("%.3f", diferenciaSegundos) + 
                              " segundos atrasado respecto al cliente");
        } else {
            System.out.println("Diferencia: Las horas son exactamente iguales");
        }
        
        // Mostramos la diferencia absoluta
        System.out.println("Diferencia absoluta: " + 
                          String.format("%.3f", Math.abs(diferenciaSegundos)) + " segundos");
        
        // Mostramos también la diferencia en horas, minutos y segundos
        long horas = Math.abs(diferenciaMs) / 3600000;
        long minutos = (Math.abs(diferenciaMs) % 3600000) / 60000;
        long segundos = (Math.abs(diferenciaMs) % 60000) / 1000;
        long milisegundos = Math.abs(diferenciaMs) % 1000;
        
        if (horas > 0 || minutos > 0 || segundos > 0) {
            System.out.print("Diferencia detallada: ");
            if (horas > 0) System.out.print(horas + " horas ");
            if (minutos > 0) System.out.print(minutos + " minutos ");
            if (segundos > 0) System.out.print(segundos + " segundos ");
            if (milisegundos > 0) System.out.print(milisegundos + " milisegundos");
            System.out.println();
        }
        
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
    } catch (java.time.format.DateTimeParseException e) {
      System.out.println("Error al parsear la fecha/hora recibida: " + e.getMessage());
    }
  }
}