package core.observers.interfaces;

public interface ISubject {
    void attach(IObserver observer);
    void detach(IObserver observer);
    void notifyObservers();
}
