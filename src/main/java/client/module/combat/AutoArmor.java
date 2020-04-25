package client.module.combat;

import client.event.EventTarget;
import client.event.events.update.EventUpdate;
import client.module.Module;
import client.setting.Setting;
import client.utils.ArmorUtils;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.lwjgl.input.Keyboard;

public class AutoArmor extends Module {
    public AutoArmor() {
        super("AutoArmor", Keyboard.KEY_NONE, Category.COMBAT);
        managers.settingManager.addSetting(new Setting(this, "Best", true));
    }

    // FIXME: WindowClickのメゾットで使ってるClickTypeがだめなのか, わからない
    @EventTarget
    public void onEnable() {
        super.onEnable();
        this.chestplate = new int[]{311, 307, 315, 303, 299};
        this.leggings = new int[]{312, 308, 316, 304, 300};
        this.boots = new int[]{313, 309, 317, 305, 301};
        this.helmet = new int[]{310, 306, 314, 302, 298};
        this.delay = 0;
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (managers.settingManager.getSetting(this, "Best").getBooleanValue()) {
            betterArmor();
        } else {
            autoArmor();
        }
    }
    public void autoArmor() {
        if (managers.settingManager.getSetting(this, "Best").getBooleanValue()) {
            return;
        }
        int item = -1;
        this.delay += 1;
        if (this.delay >= 10) {
            if (mc.thePlayer.inventory.armorInventory.get(0) == null) {
                int[] boots;
                int length = (boots = this.boots).length;
                for (int i = 0; i < length; i++) {
                    int id = boots[i];
                    if (ArmorUtils.getItem(id) != -1) {
                        item = ArmorUtils.getItem(id);
                        break;
                    }
                }
            }
            if (mc.thePlayer.inventory.armorInventory.get(1) == null) {
                int[] leggings;
                int length = (leggings = this.leggings).length;
                for (int i = 0; i < length; i++) {
                    int id = leggings[i];
                    if (ArmorUtils.getItem(id) != -1) {
                        item = ArmorUtils.getItem(id);
                        break;
                    }
                }
            }
            if (mc.thePlayer.inventory.armorInventory.get(2) == null) {
                int[] chestplate;
                int length = (chestplate = this.chestplate).length;
                for (int i = 0; i < length; i++) {
                    int id = chestplate[i];
                    if (ArmorUtils.getItem(id) != -1) {
                        item = ArmorUtils.getItem(id);
                        break;
                    }
                }
            }
            if (mc.thePlayer.inventory.armorInventory.get(3) == null) {
                int[] helmet;
                int length = (helmet = this.helmet).length;
                for (int i = 0; i < length; i++) {
                    int id = helmet[i];
                    if (ArmorUtils.getItem(id) != -1) {
                        item = ArmorUtils.getItem(id);
                        break;
                    }
                }
            }
            if (item != -1) {
                mc.playerController.windowClick(0, item, 0, ClickType.QUICK_MOVE, mc.thePlayer);
                this.delay = 0;
            }
        }
    }

    public void betterArmor() {
        if (!managers.settingManager.getSetting(this, "Best").getBooleanValue()) {
            return;
        }
        this.delay += 1;
        if ((this.delay >= 10) && ((mc.thePlayer.openContainer == null) || (mc.thePlayer.openContainer.windowId == 0))) {
            boolean switchArmor = false;
            int item = -1;
            if (mc.thePlayer.inventory.armorInventory.get(0) == null) {
                int[] array;
                int j = (array = this.boots).length;
                for (int i = 0; i < j; i++) {
                    int id = array[i];
                    if (ArmorUtils.getItem(id) != -1) {
                        item = ArmorUtils.getItem(id);
                        break;
                    }
                }
            }
            if (ArmorUtils.isBetterArmor(0, this.boots)) {
                item = 8;
                switchArmor = true;
            }
            if (mc.thePlayer.inventory.armorInventory.get(3) == null) {
                int[] array;
                int j = (array = this.helmet).length;
                for (int i = 0; i < j; i++) {
                    int id = array[i];
                    if (ArmorUtils.getItem(id) != -1) {
                        item = ArmorUtils.getItem(id);
                        break;
                    }
                }
            }
            if (ArmorUtils.isBetterArmor(3, this.helmet)) {
                item = 5;
                switchArmor = true;
            }
            if (mc.thePlayer.inventory.armorInventory.get(1) == null) {
                int[] array;
                int j = (array = this.leggings).length;
                for (int i = 0; i < j; i++) {
                    int id = array[i];
                    if (ArmorUtils.getItem(id) != -1) {
                        item = ArmorUtils.getItem(id);
                        break;
                    }
                }
            }
            if (ArmorUtils.isBetterArmor(1, this.leggings)) {
                item = 7;
                switchArmor = true;
            }
            if (mc.thePlayer.inventory.armorInventory.get(2) == null) {
                int[] array;
                int j = (array = this.chestplate).length;
                for (int i = 0; i < j; i++) {
                    int id = array[i];
                    if (ArmorUtils.getItem(id) != -1) {
                        item = ArmorUtils.getItem(id);
                        break;
                    }
                }
            }
            if (ArmorUtils.isBetterArmor(2, this.chestplate)) {
                item = 6;
                switchArmor = true;
            }
            boolean b = false;
            NonNullList<ItemStack> stackArray;
            int k = (stackArray = mc.thePlayer.inventory.mainInventory).size();
            for (int j = 0; j < k; j++) {
                ItemStack stack = stackArray.get(j);
                if (stack == null) {
                    b = true;
                    break;
                }
            }
            switchArmor = (switchArmor) && (!b);
            if (item != -1) {
                // switchArmor ? 4 : 1
                mc.playerController.windowClick(0, item, 0, switchArmor ? ClickType.PICKUP : ClickType.QUICK_MOVE, mc.thePlayer);
                this.delay = 0;
            }
        }
    }

    private int[] chestplate;
    private int[] leggings;
    private int[] boots;
    private int[] helmet;
    private int delay;
}
