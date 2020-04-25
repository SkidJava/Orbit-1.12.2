package client.event.events.player;

import client.event.Event;
import net.minecraft.potion.PotionEffect;

public class EventUpdatePotionEffect extends Event {

    public PotionEffect potionEffect;

    public EventUpdatePotionEffect(PotionEffect potionEffect) {
        this.potionEffect = potionEffect;
    }

}
