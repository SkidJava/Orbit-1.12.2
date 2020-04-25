package client.command.commands;

import client.command.Command;
import client.module.Empty;
import client.module.Module;
import client.utils.PlayerUtil;
import org.lwjgl.input.Keyboard;

public class Visible implements Command {

    @Override
    public boolean run(String[] args) {
        if (args.length == 2) {
            Module m = managers.moduleManager.getModule(args[1]);
            if (!(m instanceof Empty)) {
                m.setVisible(!m.isVisible());
                PlayerUtil.tellPlayer(m.getName() + " " + (m.isVisible() ? "shown" : "hidden") + ".");
            } else {
                PlayerUtil.tellPlayer(args[1] + " not found.");
            }
            return true;
        }
        return false;
    }

    @Override
    public String usage() {
        return "USAGE: -visible [module]";
    }

}
