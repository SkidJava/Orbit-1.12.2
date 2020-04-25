package client.utils.font;

import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class CharFont {
    private float imgSize = 1024;
    protected CharData[] charData = new CharData[256];
    protected Font font;
    protected int fontHeight = 4;
    protected int kerning;
    protected DynamicTexture tex;

    public CharFont(Font font) {
        this(font, 0);
    }

    public CharFont(Font font, int kernings) {
        this.font = font;
        this.kerning = kerning;
        tex = setupTexture(font, this.charData);
    }

    protected DynamicTexture setupTexture(Font font, CharData[] chars) {
        BufferedImage img = generateFontImage(font, chars);

        try {
            return new DynamicTexture(img);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    protected BufferedImage generateFontImage(Font font, CharData[] chars) {
        int imgSize = (int) this.imgSize;
        BufferedImage bufferedImage = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) bufferedImage.getGraphics();
        g.setFont(font);
        g.setColor(new Color(255, 255, 255, 0));
        g.fillRect(0, 0, imgSize, imgSize);
        g.setColor(Color.WHITE);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        FontMetrics fontMetrics = g.getFontMetrics();
        int charHeight = 0;
        int positionX = 0;
        int positionY = 5;

        for (int i = 0; i < chars.length; i++) {
            char ch = (char) i;
            CharData charData = new CharData();
            Rectangle2D dimensions = fontMetrics.getStringBounds(String.valueOf(ch), g);
            charData.width = (dimensions.getBounds().width + 8);
            charData.height = dimensions.getBounds().height;

            if (positionX + charData.width >= imgSize) {
                positionX = 0;
                positionY += charHeight + 3;
                charHeight = 0;
            }

            if (charData.height > charHeight) {
                charHeight = charData.height;
            }

            charData.storedX = positionX;
            charData.storedY = positionY;

            if (charData.height > this.fontHeight) {
                this.fontHeight = charData.height;
            }

            chars[i] = charData;
            g.drawString(String.valueOf(ch), positionX + 2, positionY + fontMetrics.getAscent());
            positionX += charData.width + 10;
        }

        return bufferedImage;
    }

    public void drawChar(CharData[] chars, char c, float x, float y) throws ArrayIndexOutOfBoundsException {
        try {
            drawQuad(x, y, chars[c].width, chars[c].height, chars[c].storedX, chars[c].storedY, chars[c].width, chars[c].height);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void drawQuad(float x, float y, float width, float height, float srcX, float srcY, float srcWidth, float srcHeight) {
        float renderSRCX = srcX / imgSize;
        float renderSRCY = srcY / imgSize;
        float renderSRCWidth = srcWidth / imgSize;
        float renderSRCHeight = srcHeight / imgSize;
        GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
        GL11.glVertex2d(x + width, y);
        GL11.glTexCoord2f(renderSRCX, renderSRCY);
        GL11.glVertex2d(x, y);
        GL11.glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
        GL11.glVertex2d(x, y + height);
        GL11.glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
        GL11.glVertex2d(x, y + height);
        GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY + renderSRCHeight);
        GL11.glVertex2d(x + width, y + height);
        GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
        GL11.glVertex2d(x + width, y);
    }

    public int getStringHeight(String text) {
        return getHeight();
    }

    public int getHeight() {
        return (this.fontHeight) / 4 + 2;
    }

    public int getWidth(String text) {
        int width = 0;

        for (char c : text.toCharArray()) {
            if ((c < this.charData.length) && (c >= 0)) {
                width += this.charData[c].width - 8 + this.kerning;
            }
        }

        return width / 2;
    }

    public Font getFont() {
        return this.font;
    }

    protected class CharData {
        public int width;
        public int height;
        public int storedX;
        public int storedY;

        protected CharData() {
        }
    }
}