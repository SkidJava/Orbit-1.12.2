package client.module.render.hud;

import client.ClientInfo;
import client.event.EventTarget;
import client.event.events.render.EventRender2D;
import client.module.Module;
import client.utils.ColorUtils;
import client.utils.font.CharFontRenderer;
import org.lwjgl.input.Keyboard;

public class HUD extends Module {

    protected static CharFontRenderer clientNameFont;
    protected static CharFontRenderer hudFont;

    public HUD() {
        super("HUD", Keyboard.KEY_F, Category.RENDER, false);

        clientNameFont = managers.fontManager.getClientNameFont();
        hudFont = managers.fontManager.getHudFont();
    }

    public HUD(String name, boolean visible) {
        super(name, visible);
    }

    public HUD(String name, boolean visible, boolean enabled) {
        super(name, visible, enabled);
    }

    public void onEnable() {
        super.onEnable();
    }

    @EventTarget
    public void onRedner(EventRender2D e) {
        this.renderTopString(5, 5);
    }

    private void renderTopString(int x, int y) {
        clientNameFont.drawStringWithShadow(ClientInfo.getClientName(), x, y, ColorUtils.nameColor);
        //hudS.drawStringWithShadow(ClientInfo.getClientBuild(), x + clientFont.getWidth(ClientInfo.getClientName()) + 2, y, ColorUtils.verColor);
    }
}