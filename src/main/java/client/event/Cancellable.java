package client.event;

public interface Cancellable {
    boolean isCancelled();

    void setCancelled(boolean paramBoolean);
}

