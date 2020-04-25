package client.gui.button;
import net.minecraft.client.gui.*;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButtonTexture extends GuiButton {

    private ResourceLocation tex;

    public GuiButtonTexture(int buttonId, int x, int y, int width, int height, ResourceLocation tex) {
        super(buttonId, x - width / 2, y - height / 2, width, height, "");

        this.tex = tex;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float temp) {
        if (this.visible) {
            float tempX = xPosition / scale + (width / scale - width) / 2.0f;
            float tempY = yPosition / scale + (height / scale - height) / 2.0f;
            float tempScaleX = scale;
            float tempScaleY = scale;

            this.hovered = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + this.width && mouseY < yPosition + this.height;

            if (this.hovered && this.enabled) {
                tempScaleX = tempScaleY = 0.9f;
                tempX = xPosition / tempScaleX + (width / tempScaleX - width) / 2.0f;
                tempY = yPosition / tempScaleY + (height / tempScaleY - height) / 2.0f;
            }

            GL11.glScalef(tempScaleX, tempScaleY, 1.0f);

            mc.getTextureManager().bindTexture(tex);
            GlStateManager.color(255, 255, 255, 255);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            Gui.drawScaledCustomSizeModalRect((int)tempX, (int)tempY, 0.0F, 0.0F, 1024, 1024, this.width, this.height, 1024, 1024);

            GL11.glScalef(1.0f / tempScaleX, 1.0f / tempScaleY, 1.0f);
        }
    }
}
