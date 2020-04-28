package client.module.movement.speed.components;

import client.event.EventTarget;
import client.event.events.move.EventMove;
import client.event.events.move.EventPreMotionUpdates;
import client.module.movement.speed.Speed;

import client.utils.LiquidUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;


public class Test extends Speed {
    public Test() {
        super("Test", false);
    }

    @EventTarget
    private void onMove(EventMove event) {

    }

    @EventTarget
    private void onUpdate(EventPreMotionUpdates event) {

    }
}
