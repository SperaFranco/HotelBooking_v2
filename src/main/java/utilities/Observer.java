package utilities;

public interface Observer {
    void updateAvailability(Subject subject, Object argument);
    void updateReservations(Subject subject, Object argument);
}
