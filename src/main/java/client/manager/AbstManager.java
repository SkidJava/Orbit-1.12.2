package client.manager;

import client.Client;

public abstract class AbstManager {

    protected Client client = Client.getClient();
    protected Managers managers = Managers.getManagers();

    private boolean loading = true;

    public boolean getLoading() {
        return loading;
    }

    public void load() {
    }

    public void asyncLoad() {
    }

    public void loadEnd() {
        loading = false;
    }
}
