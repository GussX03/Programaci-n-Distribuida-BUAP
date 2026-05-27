package common.protocol;

public enum CommandType {
    REGISTER,
    LOGIN,
    SUBSCRIBE,
    UNSUBSCRIBE,
    PUBLISH,
    LOGOUT,
    GET_TOPICS, // Para que el cliente pueda ver qué temas hay disponibles
    HEARTBEAT // Para mantener la conexión viva o detectar desconexiones
}