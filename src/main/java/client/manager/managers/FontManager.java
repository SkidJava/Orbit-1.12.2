package client.manager.managers;


import client.manager.AbstManager;
import client.utils.font.CharFontRenderer;
import client.utils.font.FontRendererMap;

import java.util.HashMap;
import java.util.Map;

public class FontManager extends AbstManager {

    private final String mineFont = "orbitron.ttf";
    private final int mineFontSize = 25;

    private final String clientdefFont = "orbitron.ttf";
    private final int clientdefFontSize = 25;

    private final String clientFont = "orbitron.ttf";
    private final int clientFontSize = 30;

    private final String clientNameFont = "orbitron.ttf";
    private final int clientNameFontSize = 60;

    private final String hudFont = "orbitron-m.ttf";
    private final int hudFontSize = 30;

    private Map<String, FontRendererMap> fontList = new HashMap<>();

    @Override
    public void load() {
        createDefaultFont();
    }

    private void createDefaultFont() {
        createFontData(mineFont, mineFontSize);
        createFontData(clientdefFont, clientdefFontSize);
        createFontData(clientFont, clientFontSize);
        createFontData(clientNameFont, clientNameFontSize);
        createFontData(hudFont, hudFontSize);
    }

    public void createFontData(String fontName, int size) {
        if (!fontList.containsKey(fontName)) fontList.put(fontName, new FontRendererMap(fontName, size));
        else fontList.get(fontName).createFont(size);
    }

    public CharFontRenderer getFont(String fontName, int size) {
        if (!fontList.containsKey(fontName)) createFontData(fontName, size);
        return fontList.get(fontName).getSizeFont(size);
    }

    public CharFontRenderer getClientFont() {
        if (!fontList.containsKey(clientFont)) createFontData(clientFont, clientFontSize);
        return fontList.get(clientFont).getSizeFont(clientFontSize);
    }

    public CharFontRenderer getClientNameFont() {
        if (!fontList.containsKey(clientNameFont)) createFontData(clientNameFont, clientNameFontSize);
        return fontList.get(clientNameFont).getSizeFont(clientNameFontSize);
    }

    public CharFontRenderer getHudFont() {
        if (!fontList.containsKey(hudFont)) createFontData(hudFont, hudFontSize);
        return fontList.get(hudFont).getSizeFont(hudFontSize);
    }
}
