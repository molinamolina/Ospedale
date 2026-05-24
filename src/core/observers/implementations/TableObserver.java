package core.observers.implementations;

import core.observers.interfaces.IObserver;

public class TableObserver implements IObserver {

    private final Runnable refreshAction;

    public TableObserver(Runnable refreshAction) {
        this.refreshAction = refreshAction;
    }

    @Override
    public void update() {
        if (refreshAction != null) {
            refreshAction.run();
        }
    }
}
