package client.command;

import client.manager.Managers;

public interface Command {

    Managers managers = Managers.getManagers();

    boolean run(String[] args);

    String usage();

}
