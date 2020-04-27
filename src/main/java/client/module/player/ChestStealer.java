package client.module.player;

import client.event.EventTarget;
import client.event.events.misc.EventPacketReceive;
import client.event.events.update.EventTick;
import client.module.Module;
import client.utils.PlayerUtil;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.network.play.server.SPacketWindowItems;
import org.lwjgl.input.Keyboard;

public class ChestStealer extends Module {
    public ChestStealer() {
        super("ChestStealer", Keyboard.KEY_NONE, Category.PLAYER);
    }

    private SPacketWindowItems packet;
    private boolean shouldEmptyChest;
    private int delay = 0;
    private int currentSlot;
    private int[] whitelist = { 54 };

    private int getNextSlot(Container container) {
        int i = 0;
        PlayerUtil.tellPlayer("ChestSize: " + container.inventorySlots.size());
        for (int slotAmount = container.inventorySlots.size(); i < slotAmount; i++) { // int slotAmount = (container.inventorySlots.size() == 90 ? 54 : 27)
            System.out.println(container.getInventory().get(i).isEmpty());
            if (!container.getInventory().get(i).isEmpty()) {
                PlayerUtil.tellPlayer("Position: "+i);
                return i;
            }
        }
        PlayerUtil.tellPlayer("Position: -1");
        return -1;
    }

    public boolean isContainerEmpty(Container container) {
        boolean temp = true;
        int i = 0;
        for (int slotAmount = container.inventorySlots.size() == 90 ? 54 : 27; i < slotAmount; i++) {
            if (container.getSlot(i).getHasStack()) {
                temp = false;
            }
        }
        PlayerUtil.tellPlayer("empty?: " + temp);
        return temp;
    }

    @EventTarget
    public void onTick(EventTick event) {
        if (this.mc.theWorld == null) return;
        try {
            if ((!mc.inGameHasFocus) && (this.packet != null)
                && (mc.thePlayer.openContainer.windowId == this.packet.getWindowId())
                && ((mc.currentScreen instanceof GuiChest))) {
                if (!isContainerEmpty(mc.thePlayer.openContainer)) {
                    int rec = getNextSlot(mc.thePlayer.openContainer);
                    if (this.delay >= 2) {
                        mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, rec, 0, ClickType.QUICK_MOVE, mc.thePlayer);
                        this.delay = 0;
                    }
                    this.delay += 1;
                } else {
                    mc.thePlayer.closeScreen();
                    this.packet = null;
                }
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    @EventTarget
    public void onPacketReceive(EventPacketReceive event) {
        if ((event.getPacket() instanceof SPacketWindowItems)) {
            this.packet = ((SPacketWindowItems) event.getPacket());
        }
    }
}
