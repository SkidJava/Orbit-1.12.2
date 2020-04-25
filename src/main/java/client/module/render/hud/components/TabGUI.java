package client.module.render.hud.components;

import client.event.EventTarget;
import client.event.events.misc.EventKeyboard;
import client.event.events.render.EventRender2D;
import client.module.Empty;
import client.module.Module;
import client.module.render.hud.HUD;
import client.setting.Setting;
import client.utils.ColorUtils;
import client.utils.DecimalUtil;
import client.utils.render.RenderUtils;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class TabGUI extends HUD {

    private ArrayList<Category> categoryValues = new ArrayList<>();
    private int currentCategoryIndex, currentModIndex, currentSettingIndex;
    private boolean editMode = false;

    private int screen;

    public TabGUI() {
        super("TabGUI", true, true);

        this.categoryValues.addAll(Arrays.asList(Category.values()));
    }

    public void onEnable() {
        super.onEnable();

        this.currentCategoryIndex = 0;
        this.currentModIndex = 0;
        this.currentSettingIndex = 0;
        this.screen = 0;
        this.editMode = false;
    }

    @EventTarget
    public void onRedner(EventRender2D e) {
        //if (managers.settingManager.getSetting(HUD.class, "TabGUI").getBooleanValue()) {
            float startX = 8;
            float startY = clientNameFont.getHeight() + 6;
            float rectY = 0;
            for (Category c : this.categoryValues) {
                rectY += hudFont.getHeight() + 2;
            }
            RenderUtils.drawBorderedRect(startX - 3, startY, startX + this.getWidestCategory() + 5,
                    startY + rectY, 0.5f, ColorUtils.backColor, Color.BLACK.getRGB());
            RenderUtils.drawRect(startX - 3, startY, startX,
                    startY + rectY, ColorUtils.arrayColor);
            for (Category c : this.categoryValues) {
                if (this.getCurrentCategorry().equals(c)) {
                    RenderUtils.drawRect(startX, startY, startX + this.getWidestCategory() + 5, startY + hudFont.getHeight() + 2,
                            ColorUtils.selectedColor);
                }

                String name = c.name();
                hudFont.drawStringWithShadow(name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase(),
                        startX + 2 + (this.getCurrentCategorry().equals(c) ? 2 : 0), startY + 2, -1);
                startY += hudFont.getHeight() + 2;
            }

            if (screen == 1 || screen == 2) {
                float startModsX = startX + this.getWidestCategory() + 7;
                float startModsY = clientNameFont.getHeight() + 6 + currentCategoryIndex * (hudFont.getHeight() + 2);
                RenderUtils.drawBorderedRect(startModsX, startModsY, startModsX + this.getWidestMod() + 6,
                        startModsY + this.getModsForCurrentCategory().size() * (hudFont.getHeight() + 2), 0.5f, ColorUtils.backColor, Color.BLACK.getRGB());
                for (Module m : getModsForCurrentCategory()) {
                    if (this.getCurrentModule().equals(m)) {
                        RenderUtils.drawRect(startModsX, startModsY, startModsX + this.getWidestMod() + 6,
                                startModsY + hudFont.getHeight() + 2, ColorUtils.selectedColor);
                    }
                    if (managers.moduleManager.getVisibleComponents(m).size() > 0 || managers.settingManager.getSettingsForModule(m).size() > 0)
                        RenderUtils.drawRect(startModsX + this.getWidestMod() + 4, startModsY, startModsX + this.getWidestMod() + 6,
                                startModsY + hudFont.getHeight() + 2, ColorUtils.settingsColor);
                    hudFont.drawStringWithShadow(m.getName(), startModsX + 2 + (this.getCurrentModule().equals(m) ? 2 : 0),
                            startModsY + 2, m.isEnabled() ? ColorUtils.enabledColor : ColorUtils.disabledColor);
                    startModsY += hudFont.getHeight() + 2;
                }
            }
            if (screen == 2) {
                float startComponentX = startX + this.getWidestCategory() + this.getWidestMod() + 15;
                float startComponentY = clientNameFont.getHeight() + 6 + currentCategoryIndex * (hudFont.getHeight() + 2) + currentModIndex * (hudFont.getHeight() + 2);

                ArrayList<Module> modules = getComponentForCurrentMod();

                RenderUtils.drawBorderedRect(startComponentX, startComponentY, startComponentX + Math.max(this.getWidestComponent(), getWidestSetting()) + 5,
                        startComponentY + this.getSettingForCurrentMod().size() * (hudFont.getHeight() + 2) + getComponentForCurrentMod().size() * (hudFont.getHeight() + 2), 0.5f, ColorUtils.backColor, Color.BLACK.getRGB());
                if(modules.size() > 0) {
                    for (Module module : modules) {
                            if (this.getCurrentComponent().equals(module)) {
                                RenderUtils.drawRect(startComponentX, startComponentY, startComponentX + this.getWidestComponent() + 5,
                                        startComponentY + hudFont.getHeight() + 2, ColorUtils.selectedColor);
                            }

                        hudFont.drawStringWithShadow(module.getName() + ": " + module.isEnabled(),
                                startComponentX + 2 + (this.getCurrentComponent().equals(module) ? 2 : 0), startComponentY + 2,
                                editMode && this.getCurrentComponent().equals(module) ? ColorUtils.enabledColor : ColorUtils.disabledColor);

                        startComponentY += hudFont.getHeight() + 2;
                    }
                }

                //Minecraft.getMinecraft().ingameGUI.getChatGUI().clearChatMessages();
                float startSettingX = startX + this.getWidestCategory() + this.getWidestMod() + 15;
                float startSettiny = startComponentY;

                if(getSettingForCurrentMod().size() > 0) {
                    for (Setting s : this.getSettingForCurrentMod()) {

                        if (this.getCurrentSetting().equals(s)) {
                            RenderUtils.drawRect(startSettingX, startSettiny, startSettingX + this.getWidestSetting() + 5,
                                    startSettiny + hudFont.getHeight() + 2, ColorUtils.selectedColor);
                        }
                        if (s.isBoolean()) {
                            hudFont.drawStringWithShadow(s.getName() + ": " + s.getRawBooleanValue(),
                                    startSettingX + 2 + (this.getCurrentSetting().equals(s) ? 2 : 0), startSettiny + 2,
                                    editMode && this.getCurrentSetting().equals(s) ? ColorUtils.enabledColor : ColorUtils.disabledColor);
                        } else if (s.isDigit()) {
                            hudFont.drawStringWithShadow(s.getName() + ": " + s.getCurrentValue(),
                                    startSettingX + 2 + (this.getCurrentSetting().equals(s) ? 2 : 0), startSettiny + 2,
                                    editMode && this.getCurrentSetting().equals(s) ? ColorUtils.enabledColor : ColorUtils.disabledColor);
                        } else {
                            hudFont.drawStringWithShadow(s.getName() + ": " + s.getCurrentOption(),
                                    startSettingX + 2 + (this.getCurrentSetting().equals(s) ? 2 : 0), startSettiny + 2,
                                    editMode && this.getCurrentSetting().equals(s) ? ColorUtils.enabledColor : ColorUtils.disabledColor);
                        }
                        startSettiny += hudFont.getHeight() + 2;
                    }
                }
            }
        //}
    }

    private void up() {
        if (this.currentCategoryIndex > 0 && this.screen == 0) {
            this.currentCategoryIndex--;
        } else if (this.currentCategoryIndex == 0 && this.screen == 0) {
            this.currentCategoryIndex = this.categoryValues.size() - 1;
        } else if (this.currentModIndex > 0 && this.screen == 1) {
            this.currentModIndex--;
        } else if (this.currentModIndex == 0 && this.screen == 1) {
            this.currentModIndex = this.getModsForCurrentCategory().size() - 1;
        } else if (this.currentSettingIndex > 0 && this.screen == 2 && !this.editMode) {
            this.currentSettingIndex--;
        } else if (this.currentSettingIndex == 0 && this.screen == 2 && !this.editMode) {
            this.currentSettingIndex = getComponentForCurrentMod().size() + this.getSettingForCurrentMod().size() - 1;
        }

        if (editMode) {
            if(getComponentForCurrentMod().size() > currentSettingIndex) {
                getCurrentComponent().toggle();
                return;
            }

            Setting s = this.getCurrentSetting();
            if (s.isBoolean()) {
                s.setBooleanValue(!s.getRawBooleanValue());
            } else if (s.isDigit()) {
                if (s.isOnlyInt()) {
                    s.setCurrentValue(s.getCurrentValue() + 1);
                } else {
                    s.setCurrentValue(DecimalUtil.Calc(s.getCurrentValue(), 0.1, DecimalUtil.Calculate.ADD));
                }

            } else {
                try {
                    s.setCurrentOption(s.getOptions().get(s.getCurrentOptionIndex() - 1));
                } catch (Exception e) {
                    s.setCurrentOption(s.getOptions().get(s.getOptions().size() - 1));
                }

            }
        }

    }

    private void down() {
        if (this.currentCategoryIndex < this.categoryValues.size() - 1 && this.screen == 0) {
            this.currentCategoryIndex++;
        } else if (this.currentCategoryIndex == this.categoryValues.size() - 1 && this.screen == 0) {
            this.currentCategoryIndex = 0;
        } else if (this.currentModIndex < this.getModsForCurrentCategory().size() - 1 && this.screen == 1) {
            this.currentModIndex++;
        } else if (this.currentModIndex == this.getModsForCurrentCategory().size() - 1 && this.screen == 1) {
            this.currentModIndex = 0;
        } else if (this.currentSettingIndex < getComponentForCurrentMod().size() + this.getSettingForCurrentMod().size() - 1 && this.screen == 2
                && !this.editMode) {
            this.currentSettingIndex++;
        } else if (this.currentSettingIndex == getComponentForCurrentMod().size() + this.getSettingForCurrentMod().size() - 1 && this.screen == 2
                && !this.editMode) {
            this.currentSettingIndex = 0;
        }

        if (editMode) {
            if(getComponentForCurrentMod().size() > currentSettingIndex) {
                getCurrentComponent().toggle();
                return;
            }

            Setting s = this.getCurrentSetting();
            if (s.isBoolean()) {
                s.setBooleanValue(!s.getRawBooleanValue());
            } else if (s.isDigit()) {
                if (s.isOnlyInt()) {
                    s.setCurrentValue(s.getCurrentValue() - 1);
                } else {
                    s.setCurrentValue(DecimalUtil.Calc(s.getCurrentValue(), 0.1, DecimalUtil.Calculate.SUB));
                }

            } else {
                try {
                    s.setCurrentOption(s.getOptions().get(s.getCurrentOptionIndex() + 1));
                } catch (Exception e) {
                    s.setCurrentOption(s.getOptions().get(0));
                }

            }
        }
    }

    private void right(int key) {
        if (this.screen == 0 && getModsForCurrentCategory().size() != 0) {
            this.screen = 1;
        } else if (this.screen == 1 && key == Keyboard.KEY_RETURN) {
            this.getCurrentModule().toggle();
        } else if (this.screen == 1 && (getComponentForCurrentMod().size() > 0 || this.getSettingForCurrentMod().size() > 0) && this.getCurrentModule() != null) {
            this.editMode = false;
            this.screen = 2;
        } else if (this.screen == 2 && key == Keyboard.KEY_RETURN) {
            this.editMode = !this.editMode;
        }

    }

    private void left() {
        if (this.screen == 1) {
            this.screen = 0;
            this.currentModIndex = 0;
        } else if (this.screen == 2) {
            this.screen = 1;
            this.currentSettingIndex = 0;
        }

    }

    @EventTarget
    public void onKey(EventKeyboard e) {
        switch (e.getKey()) {
            case Keyboard.KEY_UP:
                this.up();
                break;
            case Keyboard.KEY_DOWN:
                this.down();
                break;
            case Keyboard.KEY_RIGHT:
                this.right(Keyboard.KEY_RIGHT);
                break;
            case Keyboard.KEY_LEFT:
                this.left();
                break;
            case Keyboard.KEY_RETURN:
                this.right(Keyboard.KEY_RETURN);
                break;
        }
    }

    private Setting getCurrentSetting() {
        if(getComponentForCurrentMod().size() <= currentSettingIndex && getSettingForCurrentMod().size() > currentSettingIndex - getComponentForCurrentMod().size()) {
            return getSettingForCurrentMod().get(currentSettingIndex - getComponentForCurrentMod().size());
        }
        return managers.settingManager.getSetting(Empty.class, "Empty");
    }

    private ArrayList<Setting> getSettingForCurrentMod() {
        return managers.settingManager.getSettingsForModule(getCurrentModule());
    }

    private Module getCurrentComponent() {
        if(getComponentForCurrentMod().size() > currentSettingIndex)
            return getComponentForCurrentMod().get(currentSettingIndex);
        return new Empty();
    }

    private ArrayList<Module> getComponentForCurrentMod() {
        return managers.moduleManager.getVisibleComponents(getCurrentModule());
    }

    private Category getCurrentCategorry() {
        return this.categoryValues.get(this.currentCategoryIndex);
    }

    private Module getCurrentModule() {
        return getModsForCurrentCategory().get(currentModIndex);
    }

    private ArrayList<Module> getModsForCurrentCategory() {
        ArrayList<Module> mods = new ArrayList();
        Category c = getCurrentCategorry();
        for (Module m : managers.moduleManager.getMods()) {
            if (m.getCategory().equals(c)) {
                mods.add(m);
            }
        }
        return mods;
    }

    private float getWidestSetting() {
        float width = 0;
        for (Setting s : getSettingForCurrentMod()) {
            String name;
            if (s.isBoolean()) {
                name = s.getName() + ": " + s.getRawBooleanValue();

            } else if (s.isDigit()) {
                name = s.getName() + ": " + s.getCurrentValue();
            } else {
                name = s.getName() + ": " + s.getCurrentOption();
            }
            if (hudFont.getWidth(name) > width) {
                width = hudFont.getWidth(name);
            }
        }
        return width;
    }

    private float getWidestComponent() {
        float width = 0;
        for (Module module : getComponentForCurrentMod()) {
            if (hudFont.getWidth(module.getName() + ": " + module.isEnabled()) > width) {
                width = hudFont.getWidth(module.getName() + ": " + module.isEnabled());
            }
        }
        return width;
    }

    private float getWidestMod() {
        float width = 0;
        for (Module m : getModsForCurrentCategory()) {
            float cWidth = hudFont.getWidth(m.getName());
            if (cWidth > width) {
                width = cWidth;
            }
        }
        return width;
    }

    private float getWidestCategory() {
        float width = 0;
        for (Category c : this.categoryValues) {
            String name = c.name();
            float cWidth = hudFont.getWidth(
                    name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase());
            if (cWidth > width) {
                width = cWidth;
            }
        }
        return width;
    }

}
