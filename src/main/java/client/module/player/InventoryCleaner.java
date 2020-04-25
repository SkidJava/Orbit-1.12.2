package client.module.player;

import client.event.EventTarget;
import client.event.events.update.EventUpdate;
import client.module.Module;
import client.setting.Setting;
import client.utils.Timer;
import com.google.common.collect.Multimap;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import org.lwjgl.input.Keyboard;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class InventoryCleaner extends Module {
    public InventoryCleaner() {
        super("InventoryCleaner", Keyboard.KEY_NONE, Category.PLAYER);
        managers.settingManager.addSetting(new Setting(this, "Open Inv", false));
    }

    @EventTarget
    public void onEnable() {
        super.onEnable();
        this.hasNoItems = false;
        timer = new Timer();
    }

    @EventTarget
    private void onUpdate(EventUpdate event) {
        if ((mc.thePlayer.ticksExisted % 2 == 0) && (RANDOM.nextInt(2) == 0)) {
            if ((!managers.settingManager.getSetting(this, "Open Inv").getBooleanValue()) || (mc.currentScreen instanceof GuiInventory && managers.settingManager.getSetting(this, "Open Inv").getBooleanValue())) {
                if (Timer.hasReached(59L)) {
                    CopyOnWriteArrayList<Integer> uselessItems = new CopyOnWriteArrayList();
                    for (int o = 0; o < 45; o++) {
                        if (mc.thePlayer.inventoryContainer.getSlot(o).getHasStack()) {
                            ItemStack item = mc.thePlayer.inventoryContainer.getSlot(o).getStack();
                            if ((mc.thePlayer.inventory.armorItemInSlot(0) != item) &&
                                (mc.thePlayer.inventory.armorItemInSlot(1) != item) &&
                                (mc.thePlayer.inventory.armorItemInSlot(2) != item) &&
                                (mc.thePlayer.inventory.armorItemInSlot(3) != item)) {
                                if ((item != null) && (item.getItem() != null) && (Item.getIdFromItem(item.getItem()) != 0) &&
                                    (!stackIsUseful(o))) {
                                    uselessItems.add(Integer.valueOf(o));
                                }
                                this.hasNoItems = true;
                            }
                        }
                    }
                    if (!uselessItems.isEmpty()) {
                        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, ((Integer) uselessItems.get(0)).intValue(),
                            1, ClickType.THROW, mc.thePlayer);
                        uselessItems.remove(0);

                        this.timer.reset();
                    }
                }
            }
        }
    }

    private void bestSword() {
        int slotToSwitch = 0;
        float swordDamage = 0.0F;
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if ((is.getItem() instanceof ItemSword)) {
                    float swordD = ((ItemSword) is.getItem()).getDamageVsEntity(); // getItemDamage(is)
                    if (swordD > swordDamage) {
                        swordDamage = swordD;
                        slotToSwitch = i;
                    }
                }
            }
        }
        if (this.allowSwitch) {
            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slotToSwitch,
                mc.thePlayer.inventory.currentItem, ClickType.QUICK_MOVE, mc.thePlayer);

            this.allowSwitch = false;
        }
    }

    private boolean stackIsUseful(int i) {
        ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

        boolean hasAlreadyOrBetter = false;
        if (((itemStack.getItem() instanceof ItemSword)) || ((itemStack.getItem() instanceof ItemPickaxe)) ||
            ((itemStack.getItem() instanceof ItemAxe))) {
            for (int o = 0; o < 45; o++) {
                if (o != i) {
                    if (mc.thePlayer.inventoryContainer.getSlot(o).getHasStack()) {
                        ItemStack item = mc.thePlayer.inventoryContainer.getSlot(o).getStack();
                        if (((item != null) && ((item.getItem() instanceof ItemSword))) || ((item.getItem() instanceof ItemAxe)) ||
                            ((item.getItem() instanceof ItemPickaxe))) {
                            float damageFound = getItemDamage(itemStack);
                            float damageCurrent = getItemDamage(item);

                            damageFound += EnchantmentHelper.getModifierForCreature(itemStack, EnumCreatureAttribute.UNDEFINED);
                            damageCurrent += EnchantmentHelper.getModifierForCreature(item, EnumCreatureAttribute.UNDEFINED);
                            if (damageCurrent > damageFound) {
                                hasAlreadyOrBetter = true;
                                break;
                            }
                        }
                    }
                }
            }
        } else if ((itemStack.getItem() instanceof ItemArmor)) {
            for (int o = 0; o < 45; o++) {
                if (i != o) {
                    if (mc.thePlayer.inventoryContainer.getSlot(o).getHasStack()) {
                        ItemStack item = mc.thePlayer.inventoryContainer.getSlot(o).getStack();
                        if ((item != null) && ((item.getItem() instanceof ItemArmor))) {
                            List<Integer> helmet = Arrays.asList(new Integer[]{Integer.valueOf(298), Integer.valueOf(314), Integer.valueOf(302), Integer.valueOf(306), Integer.valueOf(310)});
                            List<Integer> chestplate = Arrays.asList(new Integer[]{Integer.valueOf(299), Integer.valueOf(315), Integer.valueOf(303), Integer.valueOf(307), Integer.valueOf(311)});
                            List<Integer> leggings = Arrays.asList(new Integer[]{Integer.valueOf(300), Integer.valueOf(316), Integer.valueOf(304), Integer.valueOf(308), Integer.valueOf(312)});
                            List<Integer> boots = Arrays.asList(new Integer[]{Integer.valueOf(301), Integer.valueOf(317), Integer.valueOf(305), Integer.valueOf(309), Integer.valueOf(313)});
                            if ((helmet.contains(Integer.valueOf(Item.getIdFromItem(item.getItem())))) &&
                                (helmet.contains(Integer.valueOf(Item.getIdFromItem(itemStack.getItem()))))) {
                                if (helmet.indexOf(Integer.valueOf(Item.getIdFromItem(itemStack.getItem()))) < helmet.indexOf(Integer.valueOf(Item.getIdFromItem(item.getItem())))) {
                                    hasAlreadyOrBetter = true;

                                    break;
                                }
                            } else if ((chestplate.contains(Integer.valueOf(Item.getIdFromItem(item.getItem())))) &&
                                (chestplate.contains(Integer.valueOf(Item.getIdFromItem(itemStack.getItem()))))) {
                                if (chestplate.indexOf(Integer.valueOf(Item.getIdFromItem(itemStack.getItem()))) < chestplate.indexOf(Integer.valueOf(Item.getIdFromItem(item.getItem())))) {
                                    hasAlreadyOrBetter = true;

                                    break;
                                }
                            } else if ((leggings.contains(Integer.valueOf(Item.getIdFromItem(item.getItem())))) &&
                                (leggings.contains(Integer.valueOf(Item.getIdFromItem(itemStack.getItem()))))) {
                                if (leggings.indexOf(Integer.valueOf(Item.getIdFromItem(itemStack.getItem()))) < leggings.indexOf(Integer.valueOf(Item.getIdFromItem(item.getItem())))) {
                                    hasAlreadyOrBetter = true;

                                    break;
                                }
                            } else if ((boots.contains(Integer.valueOf(Item.getIdFromItem(item.getItem())))) &&
                                (boots.contains(Integer.valueOf(Item.getIdFromItem(itemStack.getItem()))))) {
                                if (boots.indexOf(Integer.valueOf(Item.getIdFromItem(itemStack.getItem()))) < boots.indexOf(Integer.valueOf(Item.getIdFromItem(item.getItem())))) {
                                    hasAlreadyOrBetter = true;

                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        for (int o = 0; o < 45; o++) {
            if (i != o) {
                if (mc.thePlayer.inventoryContainer.getSlot(o).getHasStack()) {
                    ItemStack item = mc.thePlayer.inventoryContainer.getSlot(o).getStack();
                    if ((item != null) && (((item.getItem() instanceof ItemSword)) || ((item.getItem() instanceof ItemAxe)) ||
                        ((item.getItem() instanceof ItemBow)) || ((item.getItem() instanceof ItemFishingRod)) ||
                        ((item.getItem() instanceof ItemArmor)) || ((item.getItem() instanceof ItemAxe)) ||
                        ((item.getItem() instanceof ItemPickaxe)) || (Item.getIdFromItem(item.getItem()) == 346))) {
                        Item found = item.getItem();
                        if (Item.getIdFromItem(itemStack.getItem()) == Item.getIdFromItem(item.getItem())) {
                            hasAlreadyOrBetter = true;

                            break;
                        }
                    }
                }
            }
        }
        if (Item.getIdFromItem(itemStack.getItem()) == 367) {
            return false;
        }
        if (Item.getIdFromItem(itemStack.getItem()) == 30) {
            return true;
        }
        if (Item.getIdFromItem(itemStack.getItem()) == 259) {
            return true;
        }
        if (Item.getIdFromItem(itemStack.getItem()) == 262) {
            return true;
        }
        if (Item.getIdFromItem(itemStack.getItem()) == 264) {
            return true;
        }
        if (Item.getIdFromItem(itemStack.getItem()) == 265) {
            return true;
        }
        if (Item.getIdFromItem(itemStack.getItem()) == 346) {
            return true;
        }
        if (Item.getIdFromItem(itemStack.getItem()) == 384) {
            return true;
        }
        if (Item.getIdFromItem(itemStack.getItem()) == 345) {
            return true;
        }
        if (Item.getIdFromItem(itemStack.getItem()) == 296) {
            return true;
        }
        if (Item.getIdFromItem(itemStack.getItem()) == 336) {
            return true;
        }
        if (Item.getIdFromItem(itemStack.getItem()) == 266) {
            return true;
        }
        if (Item.getIdFromItem(itemStack.getItem()) == 280) {
            return true;
        }
        if (itemStack.hasDisplayName()) {
            return true;
        }
        if (hasAlreadyOrBetter) {
            return false;
        }
        if ((itemStack.getItem() instanceof ItemArmor)) {
            return true;
        }
        if ((itemStack.getItem() instanceof ItemAxe)) {
            return true;
        }
        if ((itemStack.getItem() instanceof ItemBow)) {
            return true;
        }
        if ((itemStack.getItem() instanceof ItemSword)) {
            return true;
        }
        if ((itemStack.getItem() instanceof ItemPotion)) {
            return true;
        }
        if ((itemStack.getItem() instanceof ItemFlintAndSteel)) {
            return true;
        }
        if ((itemStack.getItem() instanceof ItemEnderPearl)) {
            return true;
        }
        if ((itemStack.getItem() instanceof ItemBlock)) {
            return true;
        }
        if ((itemStack.getItem() instanceof ItemFood)) {
            return true;
        }
        if ((itemStack.getItem() instanceof ItemPickaxe)) {
            return true;
        }
        return false;
    }

    private float getItemDamage(ItemStack itemStack) {
        Multimap multimap = itemStack.getAttributeModifiers(EntityEquipmentSlot.MAINHAND);
        if (!multimap.isEmpty()) {
            Iterator iterator = multimap.entries().iterator();
            if (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                AttributeModifier attributeModifier = (AttributeModifier) entry.getValue();
                double damage;
                if ((attributeModifier.getOperation() != 1) && (attributeModifier.getOperation() != 2)) {
                    damage = attributeModifier.getAmount();
                } else {
                    damage = attributeModifier.getAmount() * 100.0D;
                }
                if (attributeModifier.getAmount() > 1.0D) {
                    return 1.0F + (float) damage;
                }
                return 1.0F;
            }
        }
        return 1.0F;
    }

    private boolean isValid(Item item) {
        if (blacklistedItems.contains(Integer.valueOf(Item.getIdFromItem(item)))) {
            return (!managers.settingManager.getSetting(this, "Open Inv").getBooleanValue()) || ((mc.currentScreen instanceof GuiInventory));
        }
        return false;
    }

    private void setSwordSlot() {
        float bestDamage = 1.0F;

        int bestSlot = -1;
        for (int i = 0; i < 9; i++) {
            ItemStack item = mc.thePlayer.inventory.getStackInSlot(i);
            if (item.stackSize > 0) {
                float damage = 0.0F;
                if ((item.getItem() instanceof ItemSword)) {
                    damage = ((ItemSword) item.getItem()).getDamageVsEntity();
                } else if ((item.getItem() instanceof ItemTool)) {
                    damage = ((ItemTool) item.getItem()).toolMaterial.getDamageVsEntity();
                }
                if (damage > bestDamage) {
                    bestDamage = damage;
                    bestSlot = i;
                }
            }
        }
        if ((bestSlot != -1) && (bestSlot != mc.thePlayer.inventory.currentItem)) {
            mc.thePlayer.inventory.currentItem = bestSlot;
        }
    }

    private static final Random RANDOM = new Random();
    private static List<Integer> blacklistedItems = new ArrayList();
    private boolean allowSwitch = true;
    private boolean hasNoItems;
    private Timer timer = new Timer();
}
