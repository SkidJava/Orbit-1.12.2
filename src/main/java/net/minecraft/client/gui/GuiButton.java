package net.minecraft.client.gui;

import client.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class GuiButton extends Gui
{
    public float scale = 1.0f;

    protected static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation("textures/gui/widgets.png");

    /** Button width in pixels */
    protected int width;

    /** Button height in pixels */
    protected int height;

    /** The x position of this control. */
    public int xPosition;

    /** The y position of this control. */
    public int yPosition;

    /** The string displayed on this control. */
    public String displayString;
    public int id;

    /** True if this control is enabled, false to disable. */
    public boolean enabled;

    /** Hides the button completely if false. */
    public boolean visible;
    protected boolean hovered;

    public GuiButton(int buttonId, int x, int y, String buttonText)
    {
        this(buttonId, x, y, 200, 20, buttonText);
    }

    public GuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText)
    {
        this.width = 200;
        this.height = 20;
        this.enabled = true;
        this.visible = true;
        this.id = buttonId;
        this.xPosition = x;
        this.yPosition = y;
        this.width = widthIn;
        this.height = heightIn;
        this.displayString = buttonText;
    }

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
     * this button.
     */
    protected int getHoverState(boolean mouseOver)
    {
        int i = 1;

        if (!this.enabled)
        {
            i = 0;
        }
        else if (mouseOver)
        {
            i = 2;
        }

        return i;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float temp)
    {
        if (this.visible) {
            float tempX = xPosition / scale;
            float tempY = yPosition;
            float tempScaleX = scale;
            float tempScaleY = 1.0f;

            if (this.hovered && this.enabled && !(this instanceof GuiSlider || this instanceof GuiOptionSlider || this instanceof GuiScreenOptionsSounds.Button || this instanceof GuiOptionButton)) {
                tempScaleX = tempScaleY = 0.9f;
                tempX = xPosition / tempScaleX + (width / tempScaleX - width) / 2.0f;
                tempY = yPosition / tempScaleY + (height / tempScaleY - height) / 2.0f;
            }

            GL11.glScalef(tempScaleX, tempScaleY, 1.0f);

            FontRenderer fontrenderer = mc.fontRendererObj;
            //mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + this.width && mouseY < yPosition + this.height;
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

            int j = new Color(224, 224, 224, 224).getRGB();

            if (!this.enabled) {
                j = new Color(160, 160, 160, 255).getRGB();
            } else if (this.hovered) {
                ;//j = new Color(255, 255, 160, 255).getRGB();
            }

            RenderUtils.drawBorderedRect(tempX, tempY, tempX + this.width, tempY + this.height, 1.125f, new Color(0, 0, 0, 0).getRGB(), j);


            //this.drawTexturedModalRect(tempX, tempY, 0, 46 + i * 20, this.width / 2, this.height);
            //this.drawTexturedModalRect(tempX + this.width / 2, tempY, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
            this.mouseDragged(mc, mouseX, mouseY);

            this.drawCenteredString(fontrenderer, this.displayString, (int) tempX + this.width / 2, (int) tempY + (this.height - 8) / 2, j);

            GL11.glScalef(1.0f / tempScaleX, 1.0f / tempScaleY, 1.0f);
        }
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
     */
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY)
    {
    }

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    public void mouseReleased(int mouseX, int mouseY)
    {
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    }

    /**
     * Whether the mouse cursor is currently over the button.
     */
    public boolean isMouseOver()
    {
        return this.hovered;
    }

    public void drawButtonForegroundLayer(int mouseX, int mouseY)
    {
    }

    public void playPressSound(SoundHandler soundHandlerIn)
    {
        soundHandlerIn.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    public int getButtonWidth()
    {
        return this.width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }
}