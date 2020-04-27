package client.module.render;

import client.event.EventTarget;
import client.event.events.render.EventNameTags;
import client.event.events.render.EventRender3D;
import client.manager.Managers;
import client.manager.managers.FriendManager;
import client.module.Module;
import client.setting.Setting;
import net.mcleaks.MCLeaks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import optifine.MathUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class Tags extends Module {
    public Tags() {
        super("Tags", Keyboard.KEY_NONE, Category.RENDER);
        managers.settingManager.addSetting(new Setting(this, "Armor", false));
        managers.settingManager.addSetting(new Setting(this, "Distance", 0.4F, 1F, 1F, false));
        managers.settingManager.addSetting(new Setting(this, "Scale", 0.4F, 1F, 1F, false));
    }
    private Character formatChar = new Character('§');

    @EventTarget
    private void onRender(EventRender3D event) {
        for (Entity ent : mc.theWorld.loadedEntityList) {
            if (ent != mc.thePlayer && ent instanceof EntityPlayer) {
                double posX = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * event.particlTicks - RenderManager.renderPosX;
                double posY = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * event.particlTicks - RenderManager.renderPosY + ent.height + 0.5D;
                double posZ = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * event.particlTicks - RenderManager.renderPosZ;
                String str = ent.getDisplayName().getFormattedText();
                if (Managers.getManagers().moduleManager.getModule(NameProtect.class).isEnabled())
                    str = str.replace(MCLeaks.isAltActive() ? MCLeaks.getMCName() : Minecraft.getMinecraft().getSession().getUsername(), "User");
                if (FriendManager.isFriend(ent.getName())) str = "§b" + FriendManager.getAliasName(ent.getName());
                String colorString = this.formatChar.toString();
                double health = MathUtils.round(((EntityPlayer) ent).getHealth(), 2);
                if (health >= 12.0D) {
                    colorString = colorString + "2";
                } else if (health >= 4.0D) {
                    colorString = colorString + "6";
                } else {
                    colorString = colorString + "4";
                }
                str = str + " | " + colorString + health;

                float dist = Minecraft.getMinecraft().thePlayer.getDistanceToEntity(ent) / 4.0F;
                dist = dist <= 2.0F ? 2.0F : dist;
                float scale = 0.02672f;
                final float factor = (float)((dist <= managers.settingManager.getSetting(this, "Distance").getCurrentValue()) ? (managers.settingManager.getSetting(this, "Distance").getCurrentValue() * managers.settingManager.getSetting(this, "Scale").getCurrentValue()) : (dist * managers.settingManager.getSetting(this, "Scale").getCurrentValue()));
                scale *= factor;
                GlStateManager.pushMatrix();
                GlStateManager.disableDepth();
                GlStateManager.translate(posX, posY, posZ);
                GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(-mc.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(mc.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
                GlStateManager.scale(-scale, -scale, scale);

                GlStateManager.disableLighting();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder worldRenderer = tessellator.getBuffer();
                GlStateManager.disableTexture2D();
                int stringWidth = mc.fontRendererObj.getStringWidth(str) / 2;

                worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
                worldRenderer.pos(-stringWidth - 2, -0.8D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.5F).endVertex();
                worldRenderer.pos(-stringWidth - 2, 8.8D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.5F).endVertex();
                worldRenderer.pos(stringWidth + 2, 8.8D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.5F).endVertex();
                worldRenderer.pos(stringWidth + 2, -0.8D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.5F).endVertex();
                tessellator.draw();

                GL11.glColor3f(0.0F, 0.0F, 0.0F);
                GL11.glLineWidth(1.0E-6F);
                GL11.glBegin(3);
                GL11.glVertex2d(-stringWidth - 2, -0.8D);
                GL11.glVertex2d(-stringWidth - 2, 8.8D);
                GL11.glVertex2d(-stringWidth - 2, 8.8D);
                GL11.glVertex2d(stringWidth + 2, 8.8D);
                GL11.glVertex2d(stringWidth + 2, 8.8D);
                GL11.glVertex2d(stringWidth + 2, -0.8D);
                GL11.glVertex2d(stringWidth + 2, -0.8D);
                GL11.glVertex2d(-stringWidth - 2, -0.8D);
                GL11.glEnd();

                GlStateManager.enableTexture2D();

                mc.fontRendererObj.drawString(str, -mc.fontRendererObj.getStringWidth(str) / 2, 0, -1);

                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
                if (managers.settingManager.getSetting(this, "Armor").getBooleanValue() && ((ent instanceof EntityPlayer))) {
                    List<ItemStack> itemsToRender = new ArrayList();
                    for (int i = 0; i < 5; i++) {
                        ItemStack stack = ((EntityPlayer) ent).getEquipmentInSlot(i);
                        if (stack != null) {
                            itemsToRender.add(stack);
                        }
                    }
                    int x = -(itemsToRender.size() * 8);
                    for (ItemStack stack : itemsToRender) {
                        GlStateManager.disableDepth();
                        RenderHelper.enableGUIStandardItemLighting();
                        mc.getRenderItem().zLevel = -100.0F;
                        mc.getRenderItem().renderItemIntoGUI(stack, x, -18);
                        mc.getRenderItem().renderItemOverlays(Minecraft.getMinecraft().fontRendererObj, stack,
                            x, -18);
                        mc.getRenderItem().zLevel = 0.0F;
                        RenderHelper.disableStandardItemLighting();
                        GlStateManager.enableDepth();

                        String text = "";
                        if (stack != null) {
                            int y = 0;
                            int sLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(16), stack); // sharpness
                            int fLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(20), stack); // fireAspect
                            int kLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(19), stack); // knockback
                            if (sLevel > 0) {
                                GL11.glDisable(2896);
                                drawEnchantTag("Sh" + sLevel, x, y);
                                y -= 9;
                            }
                            if (fLevel > 0) {
                                GL11.glDisable(2896);
                                drawEnchantTag("Fir" + fLevel, x, y);
                                y -= 9;
                            }
                            if (kLevel > 0) {
                                GL11.glDisable(2896);
                                drawEnchantTag("Kb" + kLevel, x, y);
                            } else if ((stack.getItem() instanceof ItemArmor)) {
                                int pLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(0), stack); // protection
                                int tLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(7), stack); // thorns
                                int uLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(34), stack); // unbreaking
                                if (pLevel > 0) {
                                    GL11.glDisable(2896);
                                    drawEnchantTag("P" + pLevel, x, y);
                                    y -= 9;
                                }
                                if (tLevel > 0) {
                                    GL11.glDisable(2896);
                                    drawEnchantTag("Th" + tLevel, x, y);
                                    y -= 9;
                                }
                                if (uLevel > 0) {
                                    GL11.glDisable(2896);
                                    drawEnchantTag("Unb" + uLevel, x, y);
                                }
                            } else if ((stack.getItem() instanceof ItemBow)) {
                                int powLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(48), stack); // power
                                int punLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(49), stack); // punch
                                int fireLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(50), stack); // flame
                                if (powLevel > 0) {
                                    GL11.glDisable(2896);
                                    drawEnchantTag("Pow" + powLevel, x, y);
                                    y -= 9;
                                }
                                if (punLevel > 0) {
                                    GL11.glDisable(2896);
                                    drawEnchantTag("Pun" + punLevel, x, y);
                                    y -= 9;
                                }
                                if (fireLevel > 0) {
                                    GL11.glDisable(2896);
                                    drawEnchantTag("Fir" + fireLevel, x, y);
                                }
                            } else if (stack.getRarity() == EnumRarity.EPIC) {
                                drawEnchantTag(this.formatChar + "lGod", x, y);
                            }
                            x += 16;
                        }
                    }
                }
                GlStateManager.popMatrix();
                GlStateManager.disableBlend();
            }
        }
    }

    private static void drawEnchantTag(String text, int x, int y) {
        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        x = (int) (x * 1.75D);
        y -= 4;
        GL11.glScalef(0.57F, 0.57F, 0.57F);
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, x, -36 - y, -1);
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }

    @EventTarget
    private void onNametagRender(EventNameTags event) {
        event.setCancelled(true);
    }
}
