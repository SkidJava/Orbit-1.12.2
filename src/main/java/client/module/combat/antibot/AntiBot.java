package client.module.combat.antibot;

import client.module.Module;
import client.setting.Setting;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class AntiBot extends Module {
    public AntiBot() {
        super("AntiBot", Keyboard.KEY_NONE, Category.COMBAT, true, true);
    }

    public AntiBot(String name, boolean visible) {
        super(name, visible);
    }

    public AntiBot(String name, boolean visible, boolean enabled) {
        super(name, visible, enabled);
    }

    public static final List<EntityPlayer> bot = new ArrayList<>();
}
