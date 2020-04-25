package client.module;

import client.manager.Managers;
import client.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

import java.util.ArrayList;

public class Module {

    protected Managers managers = Managers.getManagers();
    protected Minecraft mc = Minecraft.getMinecraft();
    protected EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

    private Module parent = this;

    private String name;
    private int keyCode;
    private boolean enabled;
    private boolean visible;
    private boolean hasMode;
    private Category category;
    private ArrayList<Module> components = new ArrayList<>();
    public float anim = 0.0f;

    public Module(String name, int keyCode, Category category) {
        this(name, keyCode, category, true);
    }

    public Module(String name, int keyCode, Category category, boolean visible) {
        this(name, keyCode, category, visible, false);
    }

    public Module(String name, int keyCode, Category category, boolean visible, boolean hasMode) {
        if(hasMode)
            managers.settingManager.addSetting(new Setting(this, "Mode", "None", new ArrayList<>()));

        this.name = name;
        this.keyCode = keyCode;
        this.category = category;
        this.enabled = false;
        this.visible = visible;
        this.hasMode = hasMode;
    }

    public Module(String name, boolean visible) {
        this(name, visible, false);
    }

    public Module(String name, boolean visible, boolean enabled) {
        this.name = name;
        this.visible = visible;
        this.enabled = enabled;
    }

    public void toggle() {

        if (!this.enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    public void onEnable() {
        enabled = true;

        if(parent == this) {
            for (Module mod : components) if(mod.isEnabled()) mod.onEnable();
        }

        if(parent == this || parent.isEnabled())
            managers.eventManager.register(this);
    }

    public void onDisable() {
        anim = 0;

        if(parent == this) {
            enabled = false;
            for (Module mod : components) mod.onDisable();
        } if (parent.isEnabled()) {
            enabled = false;
        }

        managers.eventManager.unregister(this);
    }

    public boolean isVisible() {
        return this.visible;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public String getName() {
        return name;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    public Category getCategory() {
        return category;
    }

    public void addComponent(Module parent, Module mod) {
        mod.parent = parent;
        parent.getComponents().add(mod);

        if(hasMode) {
            Setting modes = managers.settingManager.getSetting(parent, "Mode");
            if(modes == null) {
                ArrayList<String> modeName = new ArrayList<>();
                modeName.add(mod.getName());

                managers.settingManager.addSetting(new Setting(parent, "Mode", mod.isEnabled() ? mod.getName() : "None", modeName));
            } else {
                modes.getOptions().add(mod.getName());
                if(mod.isEnabled())
                    modes.setCurrentOption(mod.getName());
            }
        }
    }

    public void setVisible(boolean visib) {
        this.visible = visib;
    }

    public ArrayList<Module> getComponents() {
        return components;
    }

    public enum Category {
        COMBAT, MOVEMENT, RENDER, WORLD, PLAYER
    }
}
