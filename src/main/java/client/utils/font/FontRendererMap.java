package client.utils.font;

import client.utils.ResourceUtil;
import org.lwjgl.Sys;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class FontRendererMap {

    Map<Integer, CharFontRenderer> renderList = new HashMap<>();

    private final String fontName;
    private final int kerning;

    public FontRendererMap(String fontName, int size) {
        this(fontName, size, 0);
    }

    public FontRendererMap(String fontName, int size, int kerning) {
        this.fontName = fontName;
        this.kerning = kerning;
        createFont(size);
    }

    public void createFont(int size) {
        Font font = ResourceUtil.getFontFile(fontName);
        font = font.deriveFont(Font.PLAIN, size);
        CharFontRenderer cFontRender = new CharFontRenderer(font, kerning);
        renderList.put(size, cFontRender);
    }

    public CharFontRenderer getSizeFont(int size) {
        return renderList.get(size);
    }
}
