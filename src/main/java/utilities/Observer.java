package utilities;

public interface Observer {
    void update(Subject subject, Object argument, String message);
}
