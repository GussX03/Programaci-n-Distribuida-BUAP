package ejemplo;

import java.io.Serializable;
public class Paquete implements Serializable {
    private String id;
    private String estado;
    private String destino;

    public Paquete(String id, String destino) {
        this.id = id;
        this.destino = destino;
        this.estado = "En bodega";
    }

    // Getters y Setters
    public String toString() {
        return "Paquete[" + id + "] hacia " + destino + ". Estado: " + estado;
    }
    public void setEstado(String estado) { this.estado = estado; }
}