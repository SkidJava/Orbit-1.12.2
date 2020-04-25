package client.module.render;

import client.event.EventTarget;
import client.event.events.move.EventPreMotionUpdates;
import client.event.events.render.BoundingBoxEvent;
import client.module.Module;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

public class Freecam extends Module {
    public Freecam() {
        super("Freecam", Keyboard.KEY_NONE, Category.RENDER);
    }

    private EntityOtherPlayerMP playerCopy;
    private double startX;
    private double startY;
    private double startZ;
    private float startYaw;
    private float startPitch;

    @Override
    public void onEnable() {
        super.onEnable();
        if (this.mc.thePlayer != null) {
            this.mc.thePlayer.noClip = true;
            this.startX = this.mc.thePlayer.posX;
            this.startY = this.mc.thePlayer.posY;
            this.startZ = this.mc.thePlayer.posZ;
            this.startYaw = this.mc.thePlayer.rotationYaw;
            this.startPitch = this.mc.thePlayer.rotationPitch;
            this.playerCopy = new EntityOtherPlayerMP((World)this.mc.theWorld, this.mc.thePlayer.getGameProfile());
            this.playerCopy.inventory = this.mc.thePlayer.inventory;
            this.playerCopy.inventoryContainer = this.mc.thePlayer.inventoryContainer;
            this.playerCopy.setPositionAndRotation(this.startX, this.startY, this.startZ, this.startYaw, this.startPitch);
            this.playerCopy.rotationYawHead = this.mc.thePlayer.rotationYawHead;
            this.mc.theWorld.addEntityToWorld(-1, (Entity)this.playerCopy);
            this.mc.renderGlobal.loadRenderers();
        }
    }

    @EventTarget
    private void onPreUpdate(EventPreMotionUpdates event) {
        event.setCancelled(this.mc.thePlayer.capabilities.isFlying = true);
    }

    @EventTarget
    private void onBoundingBox(final BoundingBoxEvent event) {
        event.boundingBox = null;
    }

    @Override
    public void onDisable() {
        this.mc.thePlayer.setPositionAndRotation(this.startX, this.startY, this.startZ, this.startYaw, this.startPitch);
        this.mc.thePlayer.noClip = false;
        this.mc.theWorld.removeEntityFromWorld(-1);
        this.mc.thePlayer.capabilities.isFlying = false;
        super.onDisable();
    }
}
