package utilities;

import java.util.Vector;

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

    public void notifyObservers(String message) {
        notifyObservers(null, message);
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
            //FIXME ogni if chiama lo stesso update con gli stessi argomenti, può essere tolto
            /*if (this instanceof ReservationManager) {
                ((Observer) arrLocal[i]).update(this, arg, message);
            }
            else if(this instanceof HotelManager) {
                ((Observer) arrLocal[i]).update(this, arg, message);
            }
            else if (this instanceof CalendarManager) {
                ((Observer) arrLocal[i]).update(this, arg, message);
            }*/
            ((Observer) arrLocal[i]).update(this, arg, message);
        }
    }

    private void clearChanged() {
        changed = false;
    }

    public void setChanged() {
        changed = true;
    }


}
