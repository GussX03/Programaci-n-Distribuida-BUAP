package ejemplo;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class ClienteCalculador {

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            MotorRemoto motor = (MotorRemoto) registry.lookup("ComputeServer");

            // Definimos la tarea: Calcular números primos en un rango
            Tarea<List<Integer>> miTarea = new Tarea<List<Integer>>() {
                private int limite = 50;

                @Override
                public List<Integer> ejecutar() {
                    List<Integer> primos = new ArrayList<>();
                    for (int i = 2; i <= limite; i++) {
                        boolean esPrimo = true;
                        for (int j = 2; j <= Math.sqrt(i); j++) {
                            if (i % j == 0) { esPrimo = false; break; }
                        }
                        if (esPrimo) primos.add(i);
                    }
                    return primos;
                }
            };

            // Enviamos la tarea al servidor
            List<Integer> resultado = motor.ejecutarTarea(miTarea);
            System.out.println("Resultado recibido del servidor: " + resultado);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}