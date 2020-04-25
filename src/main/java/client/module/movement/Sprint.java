package client.module.movement;

import client.event.EventTarget;
import client.event.events.update.EventTick;
import client.event.events.update.PrePacketSendEvent;
import client.module.Module;
import client.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;
import org.lwjgl.input.Keyboard;

public class Sprint extends Module {
    public Sprint() {
        super("Sprint", Keyboard.KEY_NONE, Category.MOVEMENT, false);
        managers.settingManager.addSetting(new Setting(this, "Fake", false));
        managers.settingManager.addSetting(new Setting(this, "Strafe", true));
        managers.settingManager.addSetting(new Setting(this, "Keep", true));
        managers.settingManager.addSetting(new Setting(this, "Legit", false));
    }

    @EventTarget
    private void onTick(EventTick event) {
        if (this.canSprint()) {
            mc.thePlayer.setSprinting(true);
        }
    }

    @EventTarget
    private void onPacketSend(PrePacketSendEvent event) {
        CPacketEntityAction packet;
        if (managers.settingManager.getSetting(this, "Fake").getBooleanValue() && event.packet instanceof CPacketEntityAction &&
            ((packet = (CPacketEntityAction)event.packet).getAction() == CPacketEntityAction.Action.START_SPRINTING || packet.getAction() == CPacketEntityAction.Action.STOP_SPRINTING)) {
            event.setCancelled(true);
        }
    }

    private boolean canSprint() {
        if (!mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isSneaking() && (!managers.settingManager.getSetting(this, "Legit").getBooleanValue() ||
            managers.settingManager.getSetting(this, "Legit").getBooleanValue() && mc.thePlayer.getFoodStats().getFoodLevel() > 5)) {
            if (managers.settingManager.getSetting(this, "Strafe").getBooleanValue()) {
                return MovementInput.moveForward != 0.0f || MovementInput.moveStrafe != 0.0f;
            }
            return MovementInput.moveForward > 0.0f;
        }
        return false;
    }
}
