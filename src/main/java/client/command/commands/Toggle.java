package client.command.commands;

import client.command.Command;
import client.module.Empty;
import client.module.Module;
import client.utils.PlayerUtil;
import org.lwjgl.input.Keyboard;

public class Toggle implements Command {

    @Override
    public boolean run(String[] args) {
        if (args.length == 2) {
            Module m = managers.moduleManager.getModule(args[1]);
            if (!(m instanceof Empty)) {
                m.toggle();
                PlayerUtil.tellPlayer(m.getName() + " " + (m.isVisible() ? "enabled" : "disabled") + ".");
            } else {
                PlayerUtil.tellPlayer(args[1] + " not found.");
            }
            return true;
        }
        return false;
    }

    @Override
    public String usage() {
        return "USAGE: -toggle [module]";
    }

}
