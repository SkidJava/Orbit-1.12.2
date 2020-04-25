package client.gui;

import java.awt.*;
import java.io.IOException;
import java.util.*;

import client.manager.Managers;
import client.module.Module;
import client.module.render.GUI;
import client.setting.Setting;
import client.utils.ColorUtils;
import client.utils.font.CharFontRenderer;
import client.utils.render.RenderUtils;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class ClickGui extends GuiScreen {
    // FIXME: hehe...
    private static int selected = 0;
    private static int size = 40;
    private boolean ready;
    public void initGui() {
        this.ready = false;
        map.clear();
        map1.clear();
    }

    public void drawScreen(int x, int y, float par3) {
//        mc.currentScreen.drawDefaultBackground();
        int posX = (int) Managers.getManagers().settingManager.getSetting(GUI.class, "X").getCurrentValue();
        int posY = (int) Managers.getManagers().settingManager.getSetting(GUI.class, "Y").getCurrentValue() -size;
        int pX = posX;
        int pY = posY;
        int menuWidth = 400;

        // draw background
        RenderUtils.drawBorderedRect(pX-10, pY+size-5, pX+menuWidth, pY+(size*(icon.length+1))+5, 2, colorGen("C0C0C"), colorGen("A0A0A"));
        // window move
        if (x >= pX-10 && x <= pX+menuWidth && y >= pY+size-5 && y <= pY+size+5 && Mouse.isButtonDown(0)) {
            Managers.getManagers().settingManager.getSetting(GUI.class, "X").setCurrentValue(x);
            Managers.getManagers().settingManager.getSetting(GUI.class, "X").setCurrentValue(y);
        }

        // draw icon & box
        for (int i = 0; i < icon.length; i++) {
            final int cY = pY+=size;
            // background
            if (i == this.selected) {
                RenderUtils.drawBorderedRect(pX-10, pY, pX+size, pY+size, colorGen("C0C0C"), colorGen("303030"));
            } else {
                RenderUtils.drawRect(pX-10, pY, pX+size, pY+size, colorGen("C0C0C"));
            }
            // icon
            if (i == this.selected) {
                iconFont.drawString(icon[i], pX, cY+3, colorGen("FFFFFF"));
                smallFont.drawString(Module.Category.values()[i].name(), pX, pY+((size/4)*3), -1);
            } else {
                if (x >= pX-10 && x <= pX+size && y >= pY-10 && y <= pY+size) {
                    iconFont.drawString(icon[i], pX, cY+3, colorGen("DBDBDB"));
                    smallFont.drawString(Module.Category.values()[i].name(), pX, pY+((size/4)*3), -1);
                    if (Mouse.isButtonDown(0)) this.selected = i;
                } else {
                    iconFont.drawString(icon[i], pX, cY+3, colorGen("5B5B5B"));
                }
            }

            // positions in category
            int insideX = ((int) posX)+size+3;
            int insideY = (int) posY-7;
            // positions in no-settings module
            int underX = ((int) posX)+size+3; // insideX
            int underY = ((int) posY)+(size*icon.length)-size*2;

            // draw modules in category
            for (Module mod : Managers.getManagers().moduleManager.getModulesInCategory(Module.Category.values()[this.selected])) {
                // has settings?
                if (Managers.getManagers().settingManager.getSettingsForModule(mod) != null) {
                    // enable bind mode
                    if (x >= insideX && x <= insideX+font.getWidth(mod.getName()) && y >= insideY+7 && y <= insideY+14 && Mouse.isButtonDown(1)) {
                        this.bind = true;
                        this.bindMod = mod;
                    }

                    // draw module name
                    String text = mod.getName() + (this.bind && this.bindMod == mod ? " [...]" : (mod.getKeyCode() == -1 ? "" : " [" + Keyboard.getKeyName(mod.getKeyCode()) + "]"));
                    int srcY = insideY;
                    font.drawString(text, insideX, insideY+=7, (this.bind && this.bindMod == mod ? Color.GRAY.getRGB() : -1));

                    // toggle
                    /*Button btn = new Button(mod, insideX+6, insideY+=9); toggleButton.add(btn); btn.draw(btn.x, btn.y);*/
                    RenderUtils.drawBorderedRect(insideX, insideY+=9, insideX+6, insideY+6, mod.isEnabled() ? ColorUtils.selectedColor : colorGen("434343") , colorGen("000000"));
                    smallFont.drawString("Toggle", insideX+6, insideY, -1); // (this.bind && this.bindMod == mod ? Color.GRAY.getRGB() : -1)
                    // click
                    if (x >= insideX && x <= insideX+6 && y >= insideY && y <= insideY+6 &&
                        Mouse.isButtonDown(0) && (map.get(mod) == null || (new Date().getTime() - map.get(mod).getTime() >= 100) )) {
                        mod.toggle();
                        map.put(mod, new Date());
                    }
                    // visible
                    RenderUtils.drawBorderedRect(insideX, insideY+=9, insideX+6, insideY+6, mod.isVisible() ? ColorUtils.selectedColor : colorGen("434343") , colorGen("000000"));
                    smallFont.drawString("Visible", insideX+6, insideY, -1);
                    // click
                    if (x >= insideX && x <= insideX+6 && y >= insideY && y <= insideY+6 &&
                        Mouse.isButtonDown(0) && (map2.get(mod) == null || (new Date().getTime() - map2.get(mod).getTime() >= 100) )) {
                        mod.setVisible(!mod.isVisible());
                        map2.put(mod, new Date());
                    }

                    for (Setting op : Managers.getManagers().settingManager.getSettingsForModule(mod)) {
                        // draw options in module
                        if (op.isBoolean()) {
                            RenderUtils.drawBorderedRect(insideX+6, insideY+=9, insideX+12, insideY+6, op.getBooleanValue() ? ColorUtils.selectedColor : colorGen("434343") , colorGen("000000"));
                            smallFont.drawString(op.getName(), insideX+12, insideY, -1);
                            // click
                            if (x >= insideX+6 && x <= insideX+12 && y >= insideY && y <= insideY+6 &&
                                Mouse.isButtonDown(0) && (map1.get(op) == null || (new Date().getTime() - map1.get(op).getTime() >= 100) )) {
                                op.setBooleanValue(!op.getBooleanValue());
                                map1.put(op, new Date());
                            }
                        }

                        // draw values in module
                        if (op.isDigit()) {
                            int bar = smallFont.getWidth(mod.getName());
                            int barY = insideY+=9;
                            String str = op.getName() + ": " + op.getCurrentValue();
                            // background
                            RenderUtils.drawRect(insideX+6, barY, insideX+48, insideY+6, colorGen("434343"));
                            // bar
//                            RenderUtils.drawRect(insideX+7, barY+1, insideX+ (ここで算数のできなさがわかるぜ), insideY+5, ColorUtils.selectedColor);
                            smallFont.drawString(str, insideX+24-smallFont.getWidth(str)/2 ,barY-1, -1);
                        }
                    }

                    // move position
                    int wdt = font.getWidth(text)+15;
                    if (insideX+wdt < pX+menuWidth-wdt) {
                        insideX += wdt;
                        insideY = srcY;
                    } else {
                        insideX = ((int) posX)+size+3;
                        insideY += (insideY - srcY)+4;
                    }
                } else {
                    if (x >= underX && x <= underX+font.getWidth(mod.getName()) && y >= underY+7 && y <= underY+14 && Mouse.isButtonDown(1)) {
                        this.bind = true;
                        this.bindMod = mod;
                    }

                    String text = mod.getName() + (this.bind && this.bindMod == mod ? " [...]" : (mod.getKeyCode() == -1 ? "" : " [" + Keyboard.getKeyName(mod.getKeyCode()) + "]"));
                    int srcY = underY;

                    // toggle
                    font.drawString(text, underX, underY+=7, (this.bind && this.bindMod == mod ? Color.GRAY.getRGB() : -1));
                    RenderUtils.drawBorderedRect(underX, underY+=9, underX+6, underY+6, mod.isEnabled() ? ColorUtils.selectedColor : colorGen("434343") , colorGen("000000"));
                    smallFont.drawString("Toggle", underX+6, underY, -1);
                    // click
                    if (x >= underX && x <= underX+6 && y >= underY && y <= underY+6 &&
                        Mouse.isButtonDown(0) && (map.get(mod) == null || (new Date().getTime() - map.get(mod).getTime() >= 100) )) {
                        mod.toggle();
                        map.put(mod, new Date());
                    }

                    // visible
                    RenderUtils.drawBorderedRect(underX, underY+=9, underX+6, underY+6, mod.isVisible() ? ColorUtils.selectedColor : colorGen("434343") , colorGen("000000"));
                    smallFont.drawString("Visible", underX+6, underY, -1);
                    // click
                    if (x >= underX && x <= underX+6 && y >= underY && y <= underY+6 &&
                        Mouse.isButtonDown(0) && (map2.get(mod) == null || (new Date().getTime() - map2.get(mod).getTime() >= 100) )) {
                        mod.setVisible(!mod.isVisible());
                        map2.put(mod, new Date());
                    }

                    // move position
                    int wdt = font.getWidth(text)+15;
                    if (underX+wdt < pX+menuWidth-wdt) {
                        underX += wdt;
                        underY = srcY;
                    } else {
                        underX = ((int) posX)+size+3;
                        underY += (underY - srcY)/2;
                    }
                }
            }
        }
        // line
        RenderUtils.drawRect(((int) posX)+size-1, ((int) posY)-5, ((int) posX)+size, ((int) posY)+(size*icon.length)+5, colorGen("303030"));
        this.ready = true;
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (this.ready) {
            if (this.bind) {
                if (keyCode == Keyboard.KEY_ESCAPE || keyCode == Keyboard.KEY_DELETE) {
                    this.bind = false;
                    this.bindMod = null;
                } else {
                    if (this.bindMod != null) {
                        this.bindMod.setKeyCode(keyCode);
                    }
                    this.bind = false;
                    this.bindMod = null;
                }
            } else {
                super.keyTyped(typedChar, keyCode);
            }
        }
    }

    public int colorGen(String color) {
        if (!(color.contains("0x") || color.contains("0X") || color.contains("#"))) {
            color = "0x" + color;
        }
        return (int) ((long) Integer.decode(color).intValue() + 0xFF000000L);
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    public static Module bindMod = null;
    public static boolean bind = false;
    String[] icon = {"E", "J", "F", "D", "I", "A"};
    HashMap<Module, Date> map = new HashMap<>();
    HashMap<Module, Date> map2 = new HashMap<>();
    HashMap<Setting, Date> map1 = new HashMap<>();
    private static CharFontRenderer iconFont = Managers.getManagers().fontManager.getFont("icons.ttf", size+10);
    private static CharFontRenderer font; //= Managers.getManagers().fontManager.getFont(17);
    private static CharFontRenderer smallFont; //= Managers.getManagers().fontManager.getFont(12);
}
