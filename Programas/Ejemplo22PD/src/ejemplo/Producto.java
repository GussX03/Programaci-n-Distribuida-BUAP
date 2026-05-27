package ejemplo;

import java.io.Serializable;

public class Producto implements Serializable {
    private String nombre;
    private double precioActual;
    private String liderActual;

    public Producto(String nombre, double precioInicial) {
        this.nombre = nombre;
        this.precioActual = precioInicial;
        this.liderActual = "Nadie";
    }

    // Getters
    public String getNombre() { return nombre; }
    public double getPrecioActual() { return precioActual; }
    public String getLiderActual() { return liderActual; }

    // Setters
    public void setPrecioActual(double precio) { this.precioActual = precio; }
    public void setLiderActual(String lider) { this.liderActual = lider; }

    @Override
    public String toString() {
        return String.format("Producto: %s | Puja actual: $%.2f | Postor: %s", 
                nombre, precioActual, liderActual);
    }
}