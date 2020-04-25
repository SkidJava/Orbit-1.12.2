package client.setting;

import client.event.events.misc.EventChangeOption;
import client.module.Module;

import java.util.ArrayList;

public class Setting {

    private Module module;

    private String name, currentOption;

    private float cValue, minValue, maxValue;

    private boolean onlyInt, booleanValue;

    private ArrayList<String> options;

    private SettingMode mode;

    public Setting(Module m, String n, boolean b) {
        this.module = m;
        this.name = n;
        this.booleanValue = b;
        this.mode = SettingMode.BOOLEAN;
    }

    public Setting(Module m, String n, float cValue, float minValue, float maxValue, boolean onlyInt) {
        this.module = m;
        this.name = n;
        this.cValue = cValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.onlyInt = onlyInt;
        this.mode = SettingMode.DIGIT;
    }

    public Setting(Module m, String n, String currentOption, ArrayList<String> options) {
        this.module = m;
        this.name = n;
        this.currentOption = currentOption;
        this.options = options;
        this.mode = SettingMode.MODES;
    }

    public boolean isDigit() {
        return this.mode.equals(SettingMode.DIGIT);
    }

    public boolean isBoolean() {
        return this.mode.equals(SettingMode.BOOLEAN);
    }

    public boolean isModes() {
        return this.mode.equals(SettingMode.MODES);
    }

    public Module getModule() {
        return module;
    }

    public String getName() {
        return name;
    }

    public String getCurrentOption() {
        return currentOption;
    }

    public void setCurrentOption(String currentOption) {
        new EventChangeOption(module, currentOption, this.currentOption).call();
        this.currentOption = currentOption;
    }

    public float getCurrentValue() {
        return cValue;
    }

    public void setCurrentValue(float cValue) {
        this.cValue = cValue;
    }

    public int getCurrentOptionIndex() {
        int index = 0;
        for (String s : options) {
            if (s.equalsIgnoreCase(currentOption)) {
                return index;
            }
            index++;
        }
        return index;
    }

    public float getMinValue() {
        return minValue;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public boolean isOnlyInt() {
        return onlyInt;
    }

    public void setOnlyInt(boolean onlyInt) {
        this.onlyInt = onlyInt;
    }

    public boolean getBooleanValue() {
        return booleanValue && module.isEnabled();
    }

    public void setBooleanValue(boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public boolean getRawBooleanValue() {
        return booleanValue;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public enum SettingMode {
        BOOLEAN, DIGIT, MODES
    }

}
