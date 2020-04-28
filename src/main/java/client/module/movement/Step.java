package client.module.movement;

import client.event.EventTarget;
import client.event.events.update.EventTick;
import client.event.events.update.PrePacketSendEvent;
import client.module.Module;
import client.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;
import org.lwjgl.input.Keyboard;

public class Step extends Module {
    public Step() {
        super("Step", Keyboard.KEY_NONE, Category.MOVEMENT, true);
    }

    public int ticks = 0;

    public void onEnable() {
        ticks = 0;
        super.onEnable();
    }

    @EventTarget
    private void onTick(EventTick event) {
        EntityPlayerSP player = mc.thePlayer;
        if(player.isCollidedHorizontally) {
            switch(ticks) {
                case 0:
                    if(player.onGround)
                        player.jump();
                    break;
                case 7:
                    player.motionY = 0;
                    break;
                case 8:
                    if(!player.onGround)
                        player.setPosition(player.posX, player.posY + 2, player.posZ);
                    break;
            }
            ticks++;
        } else {
            ticks = 0;
        }
    }
}
