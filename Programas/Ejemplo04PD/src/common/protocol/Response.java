package common.protocol;

import java.io.Serializable;

public class Response implements Serializable {
    private static final long serialVersionUID = 1L;
    private ResponseType type;
    private String message; // Mensaje de éxito/error
    private Object payload; // Datos adicionales (ej: lista de temas, el objeto User, una Notificación)

    public Response(ResponseType type, String message, Object payload) {
        this.type = type;
        this.message = message;
        this.payload = payload;
    }

    public Response(ResponseType type, String message) {
        this(type, message, null);
    }

    public ResponseType getType() { return type; }
    public String getMessage() { return message; }
    public Object getPayload() { return payload; }

    @Override
    public String toString() {
        return "Response{" +
               "type=" + type +
               ", message='" + message + '\'' +
               ", payload=" + payload +
               '}';
    }
}
