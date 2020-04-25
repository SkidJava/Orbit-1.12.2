package client.command.commands;

import client.command.Command;
import client.module.Empty;
import client.module.Module;
import client.utils.PlayerUtil;
import org.lwjgl.input.Keyboard;

public class BindCommand implements Command {

    @Override
    public boolean run(String[] args) {
        if (args.length == 3) {
            Module m = managers.moduleManager.getModule(args[1]);
            if (!(m instanceof Empty)) {
                m.setKeyCode(Keyboard.getKeyIndex(args[2].toUpperCase()));
                PlayerUtil.tellPlayer(m.getName() + " has been bound to " + args[2] + ".");
            } else {
                PlayerUtil.tellPlayer(args[1] + " not found.");
            }
            return true;
        }
        return false;
    }

    @Override
    public String usage() {
        return "USAGE: -bind [module] [key]";
    }

}
