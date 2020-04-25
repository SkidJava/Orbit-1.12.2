package client.module.world;

import client.event.Event;
import client.event.EventTarget;
import client.event.events.update.EventTick;
import client.module.Module;
import client.module.combat.AutoArmor;
import client.module.combat.aura.Aura;
import client.module.movement.LongJump;
import client.module.movement.fly.Fly;
import client.module.player.ChestStealer;
import client.module.player.InventoryCleaner;
import client.setting.Setting;
import client.utils.PlayerUtil;
import net.minecraft.client.multiplayer.WorldClient;
import org.lwjgl.input.Keyboard;

public class AutoDisable extends Module {
    public AutoDisable() {
        super("AutoDisable", Keyboard.KEY_NONE, Category.WORLD);
        managers.settingManager.addSetting(new Setting(this, "KillAura", true));
        managers.settingManager.addSetting(new Setting(this, "Fly", true));
        managers.settingManager.addSetting(new Setting(this, "LongJump", true));
        managers.settingManager.addSetting(new Setting(this, "ChestStealer", true));
        managers.settingManager.addSetting(new Setting(this, "InventoryCleaner", true));
        managers.settingManager.addSetting(new Setting(this, "AutoArmor", true));
    }

    @EventTarget
    private void onWorldChange(EventTick event) {
        // !this.enabled ||
        if (mc.theWorld == null) return;
        if (mc.theWorld != world) auto();
        world = mc.theWorld;
    }

    private void auto() {
        if (managers.settingManager.getSetting(this, "KillAura").getBooleanValue() && managers.moduleManager.getModule(Aura.class).isEnabled()) {
            managers.moduleManager.getModule(Aura.class).onDisable();
        }
        if (managers.settingManager.getSetting(this, "Fly").getBooleanValue() && managers.moduleManager.getModule(Fly.class).isEnabled()) {
            managers.moduleManager.getModule(Fly.class).onDisable();
        }
        if (managers.settingManager.getSetting(this, "LongJump").getBooleanValue() && managers.moduleManager.getModule(LongJump.class).isEnabled()) {
            managers.moduleManager.getModule(LongJump.class).onDisable();
        }
        if (managers.settingManager.getSetting(this, "ChestStealer").getBooleanValue() && managers.moduleManager.getModule(ChestStealer.class).isEnabled()) {
            managers.moduleManager.getModule(ChestStealer.class).onDisable();
        }
        if (managers.settingManager.getSetting(this, "InventoryCleaner").getBooleanValue() && managers.moduleManager.getModule(InventoryCleaner.class).isEnabled()) {
            managers.moduleManager.getModule(InventoryCleaner.class).onDisable();
        }
        if (managers.settingManager.getSetting(this, "AutoArmor").getBooleanValue() && managers.moduleManager.getModule(AutoArmor.class).isEnabled()) {
            managers.moduleManager.getModule(AutoArmor.class).onDisable();
        }
        PlayerUtil.tellPlayer("Server was changed.");
    }

    WorldClient world = null;
}
