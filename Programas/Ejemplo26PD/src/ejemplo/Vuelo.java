package ejemplo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Vuelo implements Serializable {
    private String codigo;
    private int capacidad;
    private List<String> pasajeros;

    public Vuelo(String codigo, int capacidad) {
        this.codigo = codigo;
        this.capacidad = capacidad;
        this.pasajeros = new ArrayList<>();
    }

    public String getCodigo() { return codigo; }
    public int getAsientosLibres() { return capacidad - pasajeros.size(); }
    public void añadirPasajero(String nombre) { pasajeros.add(nombre); }

    @Override
    public String toString() {
        return "Vuelo " + codigo + " | Libres: " + getAsientosLibres() + "/" + capacidad;
    }
}