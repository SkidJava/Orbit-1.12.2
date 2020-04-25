package client.event;

import client.manager.Managers;

import java.lang.reflect.InvocationTargetException;

public abstract class Event {

    private boolean cancelled;

    private static void call(Event event) {
        ArrayHelper<Data> dataList = Managers.getManagers().eventManager.get(event.getClass());
        if (dataList != null) {
            for (Data data : dataList) {
                try {
                    data.target.invoke(data.source, event);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public Event call() {
        this.cancelled = false;
        call(this);
        return this;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {

        this.cancelled = cancelled;
    }

    public enum State {
        PRE("PRE", 0), POST("POST", 1);

        State(String string, int number) {
        }
    }
}