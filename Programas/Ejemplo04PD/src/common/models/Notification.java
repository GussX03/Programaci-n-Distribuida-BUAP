package common.models;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Notification implements Serializable {
    private static final long serialVersionUID = 1L;
    private String topic;
    private String message;
    private String senderUsername;
    private LocalDateTime timestamp;

    public Notification(String topic, String message, String senderUsername) {
        this.topic = topic;
        this.message = message;
        this.senderUsername = senderUsername;
        this.timestamp = LocalDateTime.now();
    }

    public String getTopic() { return topic; }
    public String getMessage() { return message; }
    public String getSenderUsername() { return senderUsername; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return "[" + timestamp.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")) +
               "] (" + topic + ") <" + senderUsername + ">: " + message;
    }
}
