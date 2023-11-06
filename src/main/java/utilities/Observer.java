package utilities;

public interface Observer {
    //TODO siccome da problemi avere diversi metodi si potrebbe fare un unico
    // update con all'interno l'oggetto e a secondo di questo chiamare l'helper method che compie l'update giusto(da discutere)
    void update(Subject subject, Object argument, String message);
}
