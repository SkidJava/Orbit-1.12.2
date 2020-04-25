package client.module.world;

import client.event.EventTarget;
import client.event.events.move.EventPreMotionUpdates;
import client.module.Module;
import client.setting.Setting;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import org.lwjgl.input.Keyboard;

public class SpinBot extends Module {
    public SpinBot() {
        super("SpinBot", Keyboard.KEY_NONE, Category.WORLD);
        managers.settingManager.addSetting(new Setting(this, "Headless", false));
        managers.settingManager.addSetting(new Setting(this, "Spinny", true));
    }
    private double increment = 25.0;
    private double serverYaw;

    @EventTarget(value=0)
    private void onUpdate(EventPreMotionUpdates event) {
        if (managers.settingManager.getSetting(this, "Spinny").getBooleanValue()) {
            this.serverYaw += this.increment;
            event.yaw = (float)this.serverYaw;
        }
        if (managers.settingManager.getSetting(this, "Headless").getBooleanValue()) {
            event.pitch = 180.0f;
        }
        if (!managers.settingManager.getSetting(this, "Headless").getBooleanValue() && !managers.settingManager.getSetting(this, "Spinny").getBooleanValue()) {
            event.yaw = (float)(Math.random() * 360.0);
            event.pitch = (float)(Math.random() * 360.0);
            this.mc.thePlayer.connection.sendPacket((Packet)new CPacketAnimation());
        }
    }
}

