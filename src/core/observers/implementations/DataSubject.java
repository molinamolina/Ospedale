package core.observers.implementations;

import core.observers.interfaces.IObserver;
import core.observers.interfaces.ISubject;
import java.util.ArrayList;
import java.util.List;

public class DataSubject implements ISubject {

    private final List<IObserver> observers = new ArrayList<>();

    @Override
    public void attach(IObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void detach(IObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (IObserver observer : observers) {
            observer.update();
        }
    }
}
