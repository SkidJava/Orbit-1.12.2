package client.module.player;

import client.event.EventTarget;
import client.event.events.update.EventUpdate;
import client.module.Module;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Keyboard;

public class FastUse extends Module {
    public FastUse() {
        super("FastUse", Keyboard.KEY_NONE, Category.PLAYER);
    }
    @EventTarget
    private void onPostUpdate(EventUpdate event) {
        if (mc.thePlayer.getItemInUseCount() == 12 && !(mc.thePlayer.getHeldItemMainhand().getItem() instanceof ItemSword) && !(mc.thePlayer.getHeldItemMainhand().getItem() instanceof ItemBow)) {
            for (int i = 0; i < 30; ++i) {
                mc.thePlayer.connection.sendPacket(new CPacketPlayer(true));
            }
            mc.getConnection().getNetworkManager().sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            mc.thePlayer.stopActiveHand();
        }
    }
}
