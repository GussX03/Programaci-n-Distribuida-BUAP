package common.protocol;

public enum ResponseType {
    SUCCESS,
    ERROR,
    NOTIFICATION // Tipo especial para cuando el servidor envía una notificación push
}