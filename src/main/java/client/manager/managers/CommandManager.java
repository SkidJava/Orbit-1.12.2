package client.manager.managers;

import client.command.Command;
import client.command.commands.*;
import client.manager.AbstManager;
import client.utils.PlayerUtil;

import java.util.HashMap;
import java.util.Map;

public class CommandManager extends AbstManager {

    private HashMap<String[], Command> commands;

    private String prefix;

    public CommandManager() {
        commands = new HashMap<>();
        prefix = "-";
    }

    @Override
    public void load() {
        commands.put(new String[]{"help", "h"}, new HelpCommand());
        commands.put(new String[]{"bind", "b"}, new BindCommand());
        commands.put(new String[]{"modules", "m"}, new ModulesCommand());
        commands.put(new String[]{"visible"}, new Visible());
        commands.put(new String[]{"toggle", "t"}, new Toggle());
    }

    public boolean processCommand(String rawMessage) {
        if (!rawMessage.startsWith(prefix)) {
            return false;
        }

        boolean safe = rawMessage.split(prefix).length > 1;

        if (safe) {
            String beheaded = rawMessage.split(prefix)[1];

            String[] args = beheaded.split(" ");

            Command command = getCommand(args[0]);

            if (command != null) {
                if (!command.run(args)) {
                    PlayerUtil.tellPlayer(command.usage());
                }
            } else {
                PlayerUtil.tellPlayer("Try -help.");
            }
        } else {
            PlayerUtil.tellPlayer("Try -help.");
        }

        return true;
    }

    private Command getCommand(String name) {
        for (Map.Entry entry : commands.entrySet()) {
            String[] key = (String[]) entry.getKey();
            for (String s : key) {
                if (s.equalsIgnoreCase(name)) {
                    return (Command) entry.getValue();
                }
            }

        }
        return null;
    }

    public HashMap<String[], Command> getCommands() {
        return commands;
    }

}
