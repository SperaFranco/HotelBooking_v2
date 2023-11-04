package utilities;

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

    public void notifyObservers() {
        notifyObservers(null);
    }

    public void notifyObservers(Object arg) {
        Object[] arrLocal;

        synchronized (this) {
            if (!changed)
                return;
            arrLocal = observers.toArray();
            clearChanged();
        }

        for (int i = arrLocal.length-1; i>=0; i--) {
            ((Observer) arrLocal[i]).updateAvailability(this, arg);
            ((Observer) arrLocal[i]).updateReservations(this, arg);
        }
    }

    private void clearChanged() {
        changed = false;
    }

    public void setChanged() {
        changed = true;
    }


}
