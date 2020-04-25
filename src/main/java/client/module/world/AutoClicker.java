package client.module.world;

import client.event.EventTarget;
import client.event.events.update.EventTick;
import client.module.Module;
import client.setting.Setting;
import client.utils.RotationUtils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.math.RayTraceResult;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.Random;

import static client.utils.Timer.getCurrentMS;

public class AutoClicker extends Module {
    public AutoClicker() {
        super("AutoClicker", Keyboard.KEY_NONE, Category.WORLD);
        managers.settingManager.addSetting(new Setting(this, "Block", true));
        managers.settingManager.addSetting(new Setting(this, "Jitter", true));

        managers.settingManager.addSetting(new Setting(this, "Min", 10F, 1F,20F, true));
        managers.settingManager.addSetting(new Setting(this, "Max", 10F, 1F,20F, true));
    }
    public void newDelay() {
        double MinCPS = managers.settingManager.getSetting(this, "Min").getCurrentValue();
        double MaxCPS = managers.settingManager.getSetting(this, "Max").getCurrentValue();
        if (MinCPS > MaxCPS) {
            MinCPS = MaxCPS;
        }
        double MinDelay = 1000.0D / MinCPS;
        double MaxDelay = 1000.0D / MaxCPS;

        double Delay = MinDelay - MaxDelay;

        this.nextDelay = (long) (MaxDelay + this.rand.nextDouble() * Delay);
    }

    @EventTarget
    public void Tick(EventTick event) {
        if (this.mc.theWorld == null || this.mc.thePlayer == null) return;
        if (managers.settingManager.getSetting(this, "Block").getBooleanValue() && this.mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) return;
        if (!this.mc.thePlayer.isEntityAlive()) return;
        if (this.mc.gameSettings.keyBindAttack.isKeyDown()) {
            if (managers.settingManager.getSetting(this, "Jitter").getBooleanValue()) {
                RotationUtils.jitterEffect(this.rand);
            }
            if (hasTimePassedMS(this.nextDelay)) {
                this.mc.leftClickCounter = 0;
                this.mc.clickMouse();
                this.mc.playerController.isHittingBlock = false;

                newDelay();
                updatebefore();
            }
        }
    }

    public boolean hasTimePassedMS(long MS) {
        return System.nanoTime() / 1000000L >= lastMS + MS;
    }

    public boolean hasTimePassedMS(long LastMS, long MS) {
        return System.nanoTime() / 1000000L >= LastMS + MS;
    }

    public void updatebefore() {
        lastMS = getCurrentMS();
    }

    protected long lastMS = -1L;
    long lastMSInventory = -1L;
    private long nextDelay = 0L;
    private Random rand = new Random();
}
