package ejemplo;

import java.io.Serializable; // Necesario para enviar objetos a través de sockets

public class Task implements Serializable {
    private static final long serialVersionUID = 1L; // Buena práctica para Serializable
    private int id;
    private String description;
    private boolean completed;

    public Task(int id, String description) {
        this.id = id;
        this.description = description;
        this.completed = false;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Descripción: '" + description + '\'' + ", Completada: " + completed;
    }
}
