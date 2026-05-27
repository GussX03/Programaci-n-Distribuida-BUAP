package ejemplo;

import java.io.Serializable;

//T es el tipo de dato que devolverá la tarea (Genéricos)
public interface Tarea<T> extends Serializable {
 T ejecutar();
}