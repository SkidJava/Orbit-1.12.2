package client.command.commands;

import client.command.Command;
import client.module.Module;
import client.utils.PlayerUtil;

public class ModulesCommand implements Command {

    @Override
    public boolean run(String[] args) {
        PlayerUtil.tellPlayer("Here are the list of moddules:");
        StringBuilder moduleList = new StringBuilder("Modules:");
        for (Module m : managers.moduleManager.getMods()) {
            moduleList.append(" ").append(m.getName()).append(",");
        }

        PlayerUtil.tellPlayer(moduleList.toString());
        return true;
    }

    @Override
    public String usage() {
        return "USAGE: -modules";
    }

}
