package client.module.render;

import client.event.EventTarget;
import client.event.events.render.EventRender3D;
import client.module.Module;
import client.setting.Setting;
import client.utils.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.tileentity.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.BossInfo;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class StorageESP extends Module {
    public StorageESP() {
        super("StorageESP", Keyboard.KEY_NONE, Category.RENDER);
    }

    @EventTarget
    private void onRender(final EventRender3D event) {
        for (final TileEntity ent : mc.theWorld.loadedTileEntityList) {
            this.drawEsp(ent, event.particlTicks);
        }
    }

    // FIXME: daze
    private void drawEsp(final TileEntity ent, final float pTicks) {
        final double x1 = ent.getPos().getX() - RenderManager.renderPosX;
        final double y1 = ent.getPos().getY() - RenderManager.renderPosY;
        final double z1 = ent.getPos().getZ() - RenderManager.renderPosZ;
        AxisAlignedBB box = new AxisAlignedBB(x1, y1, z1, x1 + 1.0, y1 + 1.0, z1 + 1.0);
        /*if (ent instanceof TileEntityChest) {
            final TileEntityChest chest = TileEntityChest.class.cast(ent);
            if (chest.adjacentChestZPos != null) {
                box = new AxisAlignedBB(x1 + 0.0625, y1, z1 + 0.0625, x1 + 0.9375, y1 + 0.875, z1 + 1.9375);
            } else if (chest.adjacentChestXPos != null) {
                box = new AxisAlignedBB(x1 + 0.0625, y1, z1 + 0.0625, x1 + 1.9375, y1 + 0.875, z1 + 0.9375);
            } else {
                if (chest.adjacentChestZPos != null || chest.adjacentChestXPos != null || chest.adjacentChestZNeg != null || chest.adjacentChestXNeg != null) {
                    return;
                }
                box = new AxisAlignedBB(x1 + 0.0625, y1, z1 + 0.0625, x1 + 0.9375, y1 + 0.875, z1 + 0.9375);
            }
        } else if (ent instanceof TileEntityEnderChest) {
            box = new AxisAlignedBB(x1 + 0.0625, y1, z1 + 0.0625, x1 + 0.9375, y1 + 0.875, z1 + 0.9375);
        }

        if (ent instanceof TileEntityChest) {
            Color color = new Color(255, 255, 0, 100);
            System.out.println("ccc");
            RenderUtils.drawSelectionBoundingBox(box, color);

        } else if (ent instanceof TileEntityBrewingStand) {
            Color color = new Color(19, 255, 0, 100);
            RenderUtils.drawSelectionBoundingBox(box, color);

        } else if (ent instanceof TileEntityEnderChest) {
            Color color = new Color(120, 5, 255, 100);
            RenderUtils.drawSelectionBoundingBox(box, color);
        }
        if (ent instanceof TileEntityFurnace) {
            Color color = new Color(110, 110, 110, 100);
            RenderUtils.drawSelectionBoundingBox(box, color);

        } else if (ent instanceof TileEntityDispenser || ent instanceof TileEntityHopper) {
            Color color = new Color(131, 240, 255, 100);
            RenderUtils.drawSelectionBoundingBox(box, color);

        } else if (ent instanceof TileEntityMobSpawner) {
            Color color = new Color(255, 169, 156, 100);
            RenderUtils.drawSelectionBoundingBox(box, color);
        }*/
    }
}
