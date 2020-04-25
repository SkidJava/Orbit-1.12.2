package client.module.movement;

import client.event.EventTarget;
import client.event.events.render.EventRender2D;
import client.module.Module;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class InventoryMove extends Module {
    public InventoryMove() {
        super("InventoryMove", Keyboard.KEY_NONE, Category.MOVEMENT);
    }
    @EventTarget
    private void onRender(EventRender2D event) {
        KeyBinding[] moveKeys = new KeyBinding[] {
            mc.gameSettings.keyBindForward,
            mc.gameSettings.keyBindBack,
            mc.gameSettings.keyBindLeft,
            mc.gameSettings.keyBindRight,
            mc.gameSettings.keyBindJump
        };
        if (mc.currentScreen instanceof GuiContainer || (mc.currentScreen != null && !GuiNewChat.getChatOpen())) {
            for (KeyBinding bind : moveKeys) {
                KeyBinding.setKeyBindState(bind.getKeyCode(), Keyboard.isKeyDown(bind.getKeyCode()));
            }
        }
    }
}
