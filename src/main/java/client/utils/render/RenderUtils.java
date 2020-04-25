package client.utils.render;

import client.event.events.render.EventRender3D;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public final class RenderUtils {
    //public static final RenderItem RENDER_ITEM = new RenderItem(Minecraft.getMinecraft().renderEngine, Minecraft.getMinecraft().modelManager);
    private static ScaledResolution scaledResolution;

    public static void drawSearchBlock(Block block, BlockPos blockPos, EventRender3D event) {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        GlStateManager.pushMatrix();
        GL11.glLineWidth(1.0f);
        GlStateManager.disableDepth();
        RenderUtils.disableLighting();
        double var8 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) event.particlTicks;
        double var10 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) event.particlTicks;
        double var12 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) event.particlTicks;
        drawOutlinedBoundingBox(block.getSelectedBoundingBox(Minecraft.getMinecraft().theWorld.getBlockState(blockPos), Minecraft.getMinecraft().theWorld, blockPos).expand(0.0020000000949949026, 0.0020000000949949026, 0.0020000000949949026).offset(-var8, -var10, -var12), -1);
        GlStateManager.popMatrix();
    }

    public static void drawEsp(EntityLivingBase ent, float pTicks, int hexColor, int hexColorIn) {
        if (!ent.isEntityAlive()) {
            return;
        }
        double x = RenderUtils.getDiff(ent.lastTickPosX, ent.posX, pTicks, RenderManager.renderPosX);
        double y = RenderUtils.getDiff(ent.lastTickPosY, ent.posY, pTicks, RenderManager.renderPosY);
        double z = RenderUtils.getDiff(ent.lastTickPosZ, ent.posZ, pTicks, RenderManager.renderPosZ);
        RenderUtils.boundingBox(ent, x, y, z, hexColor, hexColorIn);
    }

    public static void boundingBox(Entity entity, double x, double y, double z, int color, int colorIn) {
        GlStateManager.pushMatrix();
        GL11.glLineWidth(1.0f);
        AxisAlignedBB var11 = entity.getEntityBoundingBox();
        AxisAlignedBB var12 = new AxisAlignedBB(var11.minX - entity.posX + x, var11.minY - entity.posY + y, var11.minZ - entity.posZ + z, var11.maxX - entity.posX + x, var11.maxY - entity.posY + y, var11.maxZ - entity.posZ + z);
        if (color != 0) {
            GlStateManager.disableDepth();
            RenderUtils.filledBox(var12, colorIn);
            RenderUtils.disableLighting();
            drawOutlinedBoundingBox(var12, color);
        }
        GlStateManager.popMatrix();
    }

    public static void drawOutlinedBoundingBox(AxisAlignedBB bb, int color) {
        float[] color1;
        float red = (color >> 16 & 0xFF) / 255.0F;
        float blue = (color >> 8 & 0xFF) / 255.0F;
        float green = (color & 0xFF) / 255.0F;
        float alpha = (color >> 24 & 0xFF) / 255.0F;
        color1 = new float[]{red, blue, green, alpha};

        GL11.glLineWidth(1.0F);
        //GL11.glColor4f(color1[0], color1[1], color1[2], 0.8F);
        RenderGlobal.drawSelectionBoundingBox(bb, color1[0], color1[1], color1[2], 0.8F);
        GlStateManager.disableDepth();
        //GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void enableLighting() {
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glMatrixMode(5890);
        GL11.glLoadIdentity();
        float var3 = 0.0039063f;
        GL11.glScalef(var3, var3, var3);
        GL11.glTranslatef(8.0f, 8.0f, 8.0f);
        GL11.glMatrixMode(5888);
        GL11.glTexParameteri(3553, 10241, 9729);
        GL11.glTexParameteri(3553, 10240, 9729);
        GL11.glTexParameteri(3553, 10242, 10496);
        GL11.glTexParameteri(3553, 10243, 10496);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    public static void disableLighting() {
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(3553);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glDisable(2896);
        GL11.glDisable(3553);
    }

    public static void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }

    public static void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    public static void enableGL3D(float lineWidth) {
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(2884);
        Minecraft.getMinecraft().entityRenderer.disableLightmap();
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glLineWidth(lineWidth);
    }

    public static void disableGL3D() {
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glDepthMask(true);
        GL11.glCullFace(1029);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    //TODO: test
    public static void drawRect(float x, float y, float x1, float y1, int color) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        RenderUtils.glColor(color);
        RenderUtils.drawRect(x, y, x1, y1);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawBorderedRect(float x, float y, float x1, float y1, float width, int internalColor, int borderColor) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        RenderUtils.glColor(internalColor);
        RenderUtils.drawRect(x, y, x1, y1);
        RenderUtils.glColor(borderColor);
        RenderUtils.drawRect(x - width, y - width, x1 + width, y);
        RenderUtils.drawRect(x - width, y, x, y1);
        RenderUtils.drawRect(x1, y, x1 + width, y1);
        RenderUtils.drawRect(x - width, y1, x1 + width, y1 + width);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawBorderedRect(float x, float y, float x1, float y1, int insideC, int borderC) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        RenderUtils.drawVLine(x *= 2, y *= 2, (y1 *= 2) - 1, borderC);
        RenderUtils.drawVLine((x1 *= 2) - 1, y, y1, borderC);
        RenderUtils.drawHLine(x, x1 - 1, y, borderC);
        RenderUtils.drawHLine(x, x1 - 2, y1 - 1, borderC);
        RenderUtils.drawRect(x + 1, y + 1, x1 - 1, y1 - 1, insideC);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawBorderedRectReliant(float x, float y, float x1, float y1, float lineWidth, int inside, int border) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        RenderUtils.drawRect(x, y, x1, y1, inside);
        RenderUtils.glColor(border);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(lineWidth);
        GL11.glBegin(3);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x, y1);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x1, y);
        GL11.glVertex2f(x, y);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawGradientBorderedRectReliant(float x, float y, float x1, float y1, float lineWidth, int border, int bottom, int top) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        RenderUtils.drawGradientRect(x, y, x1, y1, top, bottom);
        RenderUtils.glColor(border);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(lineWidth);
        GL11.glBegin(3);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x, y1);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x1, y);
        GL11.glVertex2f(x, y);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawRoundedRect(float x, float y, float x1, float y1, int borderC, int insideC) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        RenderUtils.drawVLine(x *= 2.0f, (y *= 2.0f) + 1.0f, (y1 *= 2.0f) - 2.0f, borderC);
        RenderUtils.drawVLine((x1 *= 2.0f) - 1.0f, y + 1.0f, y1 - 2.0f, borderC);
        RenderUtils.drawHLine(x + 2.0f, x1 - 3.0f, y, borderC);
        RenderUtils.drawHLine(x + 2.0f, x1 - 3.0f, y1 - 1.0f, borderC);
        RenderUtils.drawHLine(x + 1.0f, x + 1.0f, y + 1.0f, borderC);
        RenderUtils.drawHLine(x1 - 2.0f, x1 - 2.0f, y + 1.0f, borderC);
        RenderUtils.drawHLine(x1 - 2.0f, x1 - 2.0f, y1 - 2.0f, borderC);
        RenderUtils.drawHLine(x + 1.0f, x + 1.0f, y1 - 2.0f, borderC);
        RenderUtils.drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawBorderedRect(Rectangle rectangle, float width, int internalColor, int borderColor) {
        float x = rectangle.x;
        float y = rectangle.y;
        float x1 = rectangle.x + rectangle.width;
        float y1 = rectangle.y + rectangle.height;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        RenderUtils.glColor(internalColor);
        RenderUtils.drawRect(x + width, y + width, x1 - width, y1 - width);
        RenderUtils.glColor(borderColor);
        RenderUtils.drawRect(x + 1.0f, y, x1 - 1.0f, y + width);
        RenderUtils.drawRect(x, y, x + width, y1);
        RenderUtils.drawRect(x1 - width, y, x1, y1);
        RenderUtils.drawRect(x + 1.0f, y1 - width, x1 - 1.0f, y1);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawGradientRect(float x, float y, float x1, float y1, int topColor, int bottomColor) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glShadeModel(7425);
        GL11.glBegin(7);
        RenderUtils.glColor(topColor);
        GL11.glVertex2f(x, y1);
        GL11.glVertex2f(x1, y1);
        RenderUtils.glColor(bottomColor);
        GL11.glVertex2f(x1, y);
        GL11.glVertex2f(x, y);
        GL11.glEnd();
        GL11.glShadeModel(7424);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawGradientHRect(float x, float y, float x1, float y1, int topColor, int bottomColor) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glShadeModel(7425);
        GL11.glBegin(7);
        RenderUtils.glColor(topColor);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x, y1);
        RenderUtils.glColor(bottomColor);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x1, y);
        GL11.glEnd();
        GL11.glShadeModel(7424);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawGradientRect(double x, double y, double x2, double y2, int col1, int col2) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        RenderUtils.glColor(col1);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y);
        RenderUtils.glColor(col2);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
    }

    public static void drawGradientBorderedRect(double x, double y, double x2, double y2, float l1, int col1, int col2, int col3) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glPushMatrix();
        RenderUtils.glColor(col1);
        GL11.glLineWidth(1.0f);
        GL11.glBegin(1);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        RenderUtils.drawGradientRect(x, y, x2, y2, col2, col3);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawStrip(int x, int y, float width, double angle, float points, float radius, int color) {
        float yc;
        float xc;
        float a;
        int i;
        float f1 = (float) (color >> 24 & 255) / 255.0f;
        float f2 = (float) (color >> 16 & 255) / 255.0f;
        float f3 = (float) (color >> 8 & 255) / 255.0f;
        float f4 = (float) (color & 255) / 255.0f;
        GL11.glPushMatrix();
        GL11.glTranslated((double) x, (double) y, 0.0);
        GL11.glColor4f(f2, f3, f4, f1);
        GL11.glLineWidth(width);
        if (angle > 0.0) {
            GL11.glBegin(3);
            i = 0;
            while ((double) i < angle) {
                a = (float) ((double) i * (angle * 3.141592653589793 / (double) points));
                xc = (float) (Math.cos(a) * (double) radius);
                yc = (float) (Math.sin(a) * (double) radius);
                GL11.glVertex2f(xc, yc);
                ++i;
            }
            GL11.glEnd();
        }
        if (angle < 0.0) {
            GL11.glBegin(3);
            i = 0;
            while ((double) i > angle) {
                a = (float) ((double) i * (angle * 3.141592653589793 / (double) points));
                xc = (float) (Math.cos(a) * (double) (-radius));
                yc = (float) (Math.sin(a) * (double) (-radius));
                GL11.glVertex2f(xc, yc);
                --i;
            }
            GL11.glEnd();
        }
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GL11.glDisable(3479);
        GL11.glPopMatrix();
    }

    public static void drawHLine(float x, float y, float x1, int y1) {
        if (y < x) {
            float var5 = x;
            x = y;
            y = var5;
        }
        RenderUtils.drawRect(x, x1, y + 1.0f, x1 + 1.0f, y1);
    }

    public static void drawVLine(float x, float y, float x1, int y1) {
        if (x1 < y) {
            float var5 = y;
            y = x1;
            x1 = var5;
        }
        RenderUtils.drawRect(x, y + 1.0f, x + 1.0f, x1, y1);
    }

    public static void drawHLine(float x, float y, float x1, int y1, int y2) {
        if (y < x) {
            float var5 = x;
            x = y;
            y = var5;
        }
        RenderUtils.drawGradientRect(x, x1, y + 1.0f, x1 + 1.0f, y1, y2);
    }

    public static void drawRect(float x, float y, float x1, float y1, float r, float g, float b, float a) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glColor4f(r, g, b, a);
        RenderUtils.drawRect(x, y, x1, y1);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawRect(float x, float y, float x1, float y1) {
        if (x < x1) {
            float i = x;
            x = x1;
            x1 = i;
        }

        if (y < y1) {
            float j = y;
            y = y1;
            y1 = j;
        }

        GL11.glBegin(7);

        GL11.glVertex2f(x, y1);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x1, y);
        GL11.glVertex2f(x, y);
        GL11.glEnd();
    }

    public static void drawCircle(float cx, float cy, float r, int c) {
        GL11.glPushMatrix();
        cx *= 4.0f;
        cy *= 4.0f;
        float f = (float) (c >> 24 & 255) / 255.0f;
        float f1 = (float) (c >> 16 & 255) / 255.0f;
        float f2 = (float) (c >> 8 & 255) / 255.0f;
        float f3 = (float) (c & 255) / 255.0f;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glScalef(0.25f, 0.25f, 0.25f);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glLineWidth(2.5f);
        GL11.glBegin(GL_LINE_LOOP);
        int ii = -90;
        while (ii < 270 - 360) {
            float theta = (float) (ii * 3.142 / 180);
            float p = r * 4 * (float) Math.cos(theta);
            float s = r * 4 * (float) Math.sin(theta);

            GL11.glVertex2f(p + cx, s + cy);
            ++ii;
        }

        ii = (int)(270 - 360);
        while (ii > -90) {
            float theta = (float) (ii * 3.142 / 180);
            float p = r * 4 * (float) Math.cos(theta);
            float s = r * 4 * (float) Math.sin(theta);

            GL11.glVertex2f(p + cx, s + cy);
            --ii;
        }
        GL11.glEnd();
        GL11.glScalef(4.0f, 4.0f, 4.0f);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GL11.glPopMatrix();
    }

    public static void drawCircle(float cx, float cy, float r, int c, float parcent) {
        GL11.glPushMatrix();
        cx *= 4.0f;
        cy *= 4.0f;
        float f = (float) (c >> 24 & 255) / 255.0f;
        float f1 = (float) (c >> 16 & 255) / 255.0f;
        float f2 = (float) (c >> 8 & 255) / 255.0f;
        float f3 = (float) (c & 255) / 255.0f;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glScalef(0.25f, 0.25f, 0.25f);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glLineWidth(2.5f);
        GL11.glBegin(GL_LINE_LOOP);
        int ii = -90;
        while (ii < 270 - 360 * parcent) {
            float theta = (float) (ii * 3.142 / 180);
            float p = r * 4 * (float) Math.cos(theta);
            float s = r * 4 * (float) Math.sin(theta);

            GL11.glVertex2f(p + cx, s + cy);
            ++ii;
        }

        ii = (int)(270 - 360 * parcent);
        while (ii > -90) {
            float theta = (float) (ii * 3.142 / 180);
            float p = r * 4 * (float) Math.cos(theta);
            float s = r * 4 * (float) Math.sin(theta);

            GL11.glVertex2f(p + cx, s + cy);
            --ii;
        }
        GL11.glEnd();
        GL11.glScalef(4.0f, 4.0f, 4.0f);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GL11.glPopMatrix();
    }

    public static void drawFullCircle(float cx, float cy, float r, int c) {
        r *= 2.0;
        cx *= 2;
        cy *= 2;
        float f = (float) (c >> 24 & 255) / 255.0f;
        float f1 = (float) (c >> 16 & 255) / 255.0f;
        float f2 = (float) (c >> 8 & 255) / 255.0f;
        float f3 = (float) (c & 255) / 255.0f;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glBegin(6);
        int i = 0;
        while (i <= 360) {
            double x = Math.sin((double) i * 3.141592653589793 / 180.0) * r;
            double y = Math.cos((double) i * 3.141592653589793 / 180.0) * r;
            GL11.glVertex2d((double) cx + x, (double) cy + y);
            ++i;
        }
        GL11.glEnd();
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void glColor(Color color) {
        GL11.glColor4f((float) color.getRed() / 255.0f, (float) color.getGreen() / 255.0f, (float) color.getBlue() / 255.0f, (float) color.getAlpha() / 255.0f);
    }

    public static void glColor(int hex) {
        float alpha = (float) (hex >> 24 & 255) / 255.0f;
        float red = (float) (hex >> 16 & 255) / 255.0f;
        float green = (float) (hex >> 8 & 255) / 255.0f;
        float blue = (float) (hex & 255) / 255.0f;
        GlStateManager.color(red, green, blue, alpha);
    }

    public static void glColor(float alpha, int redRGB, int greenRGB, int blueRGB) {
        float red = 0.003921569f * (float) redRGB;
        float green = 0.003921569f * (float) greenRGB;
        float blue = 0.003921569f * (float) blueRGB;
        GL11.glColor4f(red, green, blue, alpha);
    }

    public static void updateScaledResolution() {
        scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
    }

    public static ScaledResolution getScaledResolution() {
        return scaledResolution;
    }

    public static void prepareScissorBox(float x, float y, float x2, float y2) {
        RenderUtils.updateScaledResolution();
        int factor = scaledResolution.getScaleFactor();
        GL11.glScissor((int) (x * (float) factor), (int) (((float) scaledResolution.getScaledHeight() - y2) * (float) factor), (int) ((x2 - x) * (float) factor), (int) ((y2 - y) * (float) factor));
    }

    public static void drawOutlinedBox(AxisAlignedBB boundingBox) {
        if (boundingBox == null) {
            return;
        }
        GL11.glBegin(3);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxZ, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxZ, boundingBox.maxZ, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxZ, boundingBox.maxZ, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxZ, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxZ, boundingBox.maxZ);
        GL11.glEnd();
        GL11.glBegin(3);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxZ, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxZ, boundingBox.maxZ, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxZ, boundingBox.maxZ, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxZ, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxZ, boundingBox.maxZ);
        GL11.glEnd();
        GL11.glBegin(1);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxZ, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxZ, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxZ, boundingBox.maxZ, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxZ, boundingBox.maxZ, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxZ, boundingBox.maxZ, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxZ, boundingBox.maxZ, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxZ, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxZ, boundingBox.maxZ);
        GL11.glEnd();
    }

    public static void drawBox(RenderBox box) {
        GL11.glEnable(1537);
        if (box == null) {
            return;
        }
        GL11.glBegin(7);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
        GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
        GL11.glEnd();
    }

    public static void filledBox(AxisAlignedBB bb, int color) {
        float var11 = (float) (color >> 24 & 255) / 255.0f;
        float var6 = (float) (color >> 16 & 255) / 255.0f;
        float var7 = (float) (color >> 8 & 255) / 255.0f;
        float var8 = (float) (color & 255) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        tessellator.draw();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        tessellator.draw();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        tessellator.draw();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        tessellator.draw();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        tessellator.draw();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        tessellator.draw();

    }

    private static double getDiff(double lastI, double i, float ticks, double ownI) {
        return lastI + (i - lastI) * (double) ticks - ownI;
    }

    public static void drawBeacon(BlockPos pos, int color, int colorIn, float partialTicks) {
        GlStateManager.pushMatrix();
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) partialTicks;
        double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) partialTicks;
        double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) partialTicks;
        GL11.glLineWidth(1.0f);
        AxisAlignedBB var11 = new AxisAlignedBB((double) (pos.getX() + 1), (double) pos.getY(), (double) (pos.getZ() + 1), (double) pos.getX(), (double) (pos.getY() + 200), (double) pos.getZ());
        AxisAlignedBB var12 = new AxisAlignedBB(var11.minX - x, var11.minY - y, var11.minZ - z, var11.maxX - x, var11.maxY - y, var11.maxZ - z);
        if (color != 0) {
            GlStateManager.disableDepth();
            RenderUtils.filledBox(var12, colorIn);
            RenderUtils.disableLighting();
            drawOutlinedBoundingBox(var12, color);
        }
        GlStateManager.popMatrix();
    }

    public static void drawProgressBar(float x, float y, float width, float height, float progress, String progressName, int color) {
        float x1 = x - width, x2 = x + width, y1 = y - height, y2 = y + height;
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(progressName, x - Minecraft.getMinecraft().fontRendererObj.getStringWidth(progressName) / 2, y1 - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT, -1);
        drawBorderedRect(x1, y1, x2, y2, 0.5f, Color.BLACK.getRGB(), Color.BLACK.getRGB());
        drawBorderedRect(x1, y1, x1 + width * 2 * (progress * 0.01f), y2, 0.5f, color, Color.BLACK.getRGB());
    }

    public static void drawRectangleBorder(double left, double top, double right, double bottom, float borderWidth, int borderColor)
    {
        float alpha = 1.0F;
        float red = (borderColor >> 16 & 0xFF) / 255.0F;
        float green = (borderColor >> 8 & 0xFF) / 255.0F;
        float blue = (borderColor & 0xFF) / 255.0F;
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(red, green, blue, alpha);
        if (borderWidth == 1.0F) {
            GL11.glEnable(2848);
        }
        GL11.glLineWidth(borderWidth);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldRenderer = tessellator.getBuffer();
        worldRenderer.begin(1, DefaultVertexFormats.POSITION);
        worldRenderer.pos(left, top, 0.0D).endVertex();
        worldRenderer.pos(left, bottom, 0.0D).endVertex();
        worldRenderer.pos(right, bottom, 0.0D).endVertex();
        worldRenderer.pos(right, top, 0.0D).endVertex();
        worldRenderer.pos(left, top, 0.0D).endVertex();
        worldRenderer.pos(right, top, 0.0D).endVertex();
        worldRenderer.pos(left, bottom, 0.0D).endVertex();
        worldRenderer.pos(right, bottom, 0.0D).endVertex();
        tessellator.draw();
        GL11.glLineWidth(2.0F);
        if (borderWidth == 1.0F) {
            GL11.glDisable(2848);
        }
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}

