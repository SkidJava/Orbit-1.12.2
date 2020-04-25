package client.command.commands;

import client.command.Command;
import client.utils.PlayerUtil;

public class HelpCommand implements Command {

    @Override
    public boolean run(String[] args) {
        PlayerUtil.tellPlayer("Here are the list of commands:");
        for (Command c : managers.commandManager.getCommands().values()) {
            PlayerUtil.tellPlayer(c.usage());
        }
        return true;
    }

    @Override
    public String usage() {
        return "USAGE: -help";
    }

}
