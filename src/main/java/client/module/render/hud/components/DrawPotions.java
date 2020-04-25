package client.module.render.hud.components;

import client.event.EventTarget;
import client.event.events.player.EventUpdatePotionEffect;
import client.event.events.render.EventRender2D;
import client.module.render.hud.HUD;
import client.utils.ColorUtils;
import client.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;

public class DrawPotions extends HUD {

    private ResourceLocation inventoryBackground = new ResourceLocation("textures/gui/container/inventory.png");
    private HashMap<Potion, Integer> potionDuration = new HashMap<>();

    public DrawPotions() {
        super("DrawPotions", true, true);
    }

    @EventTarget
    public void onUpdatePotionEffect(EventUpdatePotionEffect event) {
        if(potionDuration.containsKey(event.potionEffect.getPotion())) {
            if (event.potionEffect.getDuration() / 20 > potionDuration.get(event.potionEffect.getPotion()))
                potionDuration.put(event.potionEffect.getPotion(), event.potionEffect.getDuration() / 20);
        } else{
            potionDuration.put(event.potionEffect.getPotion(), event.potionEffect.getDuration() / 20);
        }
    }


    @EventTarget
    public void onRender(EventRender2D e) {
        //if (managers.settingManager.getSetting(HUD.class, "PotionEffect").getBooleanValue()) {

            int x = e.width - 20;
            for (PotionEffect p : Minecraft.getMinecraft().thePlayer.getActivePotionEffects()) {
                Potion potion = p.getPotion();
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                if (potion.hasStatusIcon()) {
                    int i = p.getDuration() / 20;
                    int j = i / 60;
                    i = i % 60;

                    if(!potionDuration.containsKey(potion)) potionDuration.put(potion, p.getDuration() / 20);
                    float parcent = (float)p.getDuration() / 20 / (float)potionDuration.get(potion);

                    RenderUtils.drawFullCircle(x + 5, e.height - 11, 10, ColorUtils.backColor);
                    RenderUtils.drawCircle(x + 5, e.height - 11, 10, ColorUtils.arrayColor, parcent);
                    GL11.glColor4f(1, 1, 1, 1);

                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    GlStateManager.disableLighting();

                    int i1 = potion.getStatusIconIndex();
                    GL11.glScalef(0.8f, 0.8f, 0.8f);
                    GlStateManager.enableBlend();
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    Minecraft.getMinecraft().getTextureManager().bindTexture(inventoryBackground);
                    drawTexturedModalRect((int) (x * 1.25f) - 3, (int) (e.height * 1.25f) - 23, 0 + i1 % 8 * 18,
                            198 + i1 / 8 * 18, 18, 18);
                    GL11.glScalef(1.25f, 1.25f, 1.25f);
                    GlStateManager.disableBlend();

                    String time = (i < 10 ? j + ":0" + i : j + ":" + i);

                    if (p.getDuration() >= 32000) {
                        time = "xx:xx";
                    }

                    hudFont.drawStringWithShadow(time, x - hudFont.getWidth(time) + 12,
                            e.height - hudFont.getHeight() - 2, -1);
                    if (p.getAmplifier() != 0)
                        hudFont.drawStringWithShadow((p.getAmplifier() + 1) + "",
                                x - hudFont.getWidth((p.getAmplifier() + 1) + "") + 7,
                                e.height - hudFont.getHeight() - 13, -1);
                }

                if (Minecraft.getMinecraft().thePlayer.getActivePotionEffects().size() > 5)
                    x -= 90 / (Minecraft.getMinecraft().thePlayer.getActivePotionEffects().size() - 1);
                else
                    x -= 24;
            }
       // }
    }

    public static void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((double)(x + 0), (double)(y + height), 0).tex((double)((float)(textureX + 0) * 0.00390625F), (double)((float)(textureY + height) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double)(x + width), (double)(y + height), 0).tex((double)((float)(textureX + width) * 0.00390625F), (double)((float)(textureY + height) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double)(x + width), (double)(y + 0), 0).tex((double)((float)(textureX + width) * 0.00390625F), (double)((float)(textureY + 0) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double)(x + 0), (double)(y + 0), 0).tex((double)((float)(textureX + 0) * 0.00390625F), (double)((float)(textureY + 0) * 0.00390625F)).endVertex();
        tessellator.draw();
    }
}
