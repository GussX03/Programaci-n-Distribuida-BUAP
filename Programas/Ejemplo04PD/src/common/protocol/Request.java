package common.protocol;

import java.io.Serializable;
import java.util.Map;

public class Request implements Serializable {
    private static final long serialVersionUID = 1L;
    private CommandType command;
    private Map<String, Object> data; // Datos asociados al comando (ej: username, password, topic, message)

    public Request(CommandType command, Map<String, Object> data) {
        this.command = command;
        this.data = data;
    }

    public CommandType getCommand() { return command; }
    public Map<String, Object> getData() { return data; }

    @Override
    public String toString() {
        return "Request{" +
               "command=" + command +
               ", data=" + data +
               '}';
    }
}
