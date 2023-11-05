package utilities;

import service_layer.HotelManager;
import service_layer.ReservationManager;

import java.util.Vector;
import java.util.Observable;
public abstract class Subject {
    private boolean changed = false;
    private Vector<Observer> observers;

    public Subject() {
        observers = new Vector<>();
    }
    public synchronized void addObserver(Observer o) {
        if (o == null)
            throw new NullPointerException();
        if (!observers.contains(o)) {
            observers.addElement(o);
        }
    }

    public synchronized void removeObserver(Observer o) {
        observers.remove(o);
    }

    public void notifyObservers(String messagge) {
        notifyObservers(null, messagge);
    }

    public void notifyObservers(Object arg, String message) {
        Object[] arrLocal;

        synchronized (this) {
            if (!changed)
                return;
            arrLocal = observers.toArray();
            clearChanged();
        }

        for (int i = arrLocal.length-1; i>=0; i--) {
            if (this instanceof ReservationManager) {
                ((Observer) arrLocal[i]).updateAvailability(this, arg);
                ((Observer) arrLocal[i]).updateReservations(this, arg, message);
            }
            else if(this instanceof HotelManager) {
                ((Observer) arrLocal[i]).updateHotels(this, arg, message);

            }
        }
    }

    private void clearChanged() {
        changed = false;
    }

    public void setChanged() {
        changed = true;
    }


}
