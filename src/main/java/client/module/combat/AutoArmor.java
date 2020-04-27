package client.module.combat;

import client.Client;
import client.event.EventTarget;
import client.event.events.update.EventUpdate;
import client.module.Module;
import client.setting.Setting;
import client.utils.ArmorUtils;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.lwjgl.input.Keyboard;

public class AutoArmor extends Module {

    private enum ArmorType {

        BOOTS(0, 8),
        LEGS(1, 7),
        CHEST(2, 6),
        HELMET(3, 5);

        ArmorType(int id, int slotID) {
            this.id = id;
            this.slotID = slotID;
        }

        private int id;
        private int slotID;

        public int getID() {
            return id;
        }
        
        public int getSlotID() {
            return slotID;
        }
    }

    private int[] chest;
    private int[] legs;
    private int[] boots;
    private int[] helmet;
    private ArmorType currentTarget = ArmorType.BOOTS;

    public AutoArmor() {
        super("AutoArmor", Keyboard.KEY_NONE, Category.COMBAT);

        this.chest = new int[]{311, 307, 315, 303, 299};
        this.legs = new int[]{312, 308, 316, 304, 300};
        this.boots = new int[]{313, 309, 317, 305, 301};
        this.helmet = new int[]{310, 306, 314, 302, 298};
    }

    @EventTarget
    public void onEnable() {
        super.onEnable();
    }

    //0 = boots, 1 = legs, 2 = chest, 3 = Helmet
    @EventTarget
    public void onUpdate(EventUpdate event) {
        //System.out.println(mc.thePlayer.inventory.armorInventory.get(ArmorType.BOOTS.getID()).getEnchantmentTagList());

        int bestArmor = -1;

        for (int i = 0; i < 45; i++) {
            ItemStack item = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (item.isEmpty()) continue;

            if(isArmor(Item.getIdFromItem(item.getItem()))) {
                if (bestArmor == -1) {
                    bestArmor = i;
                    continue;
                }

                ItemStack bestItem = mc.thePlayer.inventoryContainer.getSlot(bestArmor).getStack();
                int bestScore = getArmorScore(Item.getIdFromItem(bestItem.getItem()));
                int tempScore = getArmorScore(Item.getIdFromItem(item.getItem()));

                if (tempScore > bestScore) bestArmor = i;
            }
        }

        if(bestArmor > 8) {
            ItemStack item = mc.thePlayer.inventory.armorInventory.get(currentTarget.getID());
            if(item.isEmpty()) mc.playerController.windowClick(0, currentTarget.getSlotID(), 0, ClickType.QUICK_MOVE, mc.thePlayer);
            else {
                mc.playerController.windowClick(0, bestArmor, 0, ClickType.QUICK_MOVE, mc.thePlayer);
                currentTarget = getArmorType(currentTarget.getID() + 1 % 4);
            }
        } else currentTarget = getArmorType((currentTarget.getID() + 1) % 4);
    }

    public ArmorType getArmorType(int id) {
        if(id == 0) return ArmorType.BOOTS;
        else if(id == 1) return  ArmorType.LEGS;
        else if(id == 2) return ArmorType.CHEST;
        else return ArmorType.HELMET;
    }

    private int getArmorScore(int id) {
        if(id <= 301) return 0;
        else if(id <= 305) return 1;
        else if(id <= 309) return 2;
        else if(id <= 313) return 4;
        else return 3;
    }

    private boolean isArmor(int id) {
        if(currentTarget == ArmorType.BOOTS && (id == boots[0] || id == boots[1] || id == boots[2] || id == boots[3] || id == boots[4]))
            return true;
        else if(currentTarget == ArmorType.LEGS &&(id == legs[0] || id == legs[1] || id == legs[2] || id == legs[3] || id == legs[4]))
            return true;
        else if(currentTarget == ArmorType.CHEST && (id == chest[0] || id == chest[1] || id == chest[2] || id == chest[3] || id == chest[4]))
            return true;
        else if(currentTarget == ArmorType.HELMET && (id == helmet[0] || id == helmet[1] || id == helmet[2] || id == helmet[3] || id == helmet[4]))
            return true;
        else return false;
    }
}
