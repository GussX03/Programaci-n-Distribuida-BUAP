package ejemplo;

public class Task implements Comparable<Task> {
    private final String description;
    private final int priority;
    private final TaskClient client;

    public Task(String description, int priority, TaskClient client) {
        this.description = description;
        this.priority = priority;
        this.client = client;
    }

    public String getDescription() { return description; }
    public int getPriority() { return priority; }
    public TaskClient getClient() { return client; }

    @Override
    public int compareTo(Task other) {
        return Integer.compare(other.priority, this.priority); // mayor prioridad primero
    }
}