package utilities;

import java.util.Vector;

public abstract class Subject {
    private boolean changed = false;
    private final Vector<Observer> observers;

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
    protected void notifyObservers(String message) {
        notifyObservers(null, message);
    }
    protected void notifyObservers(Object arg, String message) {
        Object[] arrLocal;

        synchronized (this) {
            if (!changed)
                return;
            arrLocal = observers.toArray();
            clearChanged();
        }
        for (int i = arrLocal.length-1; i>=0; i--) {
            ((Observer) arrLocal[i]).update(this, arg, message);
        }
    }
    private void clearChanged() {
        changed = false;
    }
    protected void setChanged() {
        changed = true;
    }
}
