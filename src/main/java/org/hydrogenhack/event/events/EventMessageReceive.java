package org.hydrogenhack.event.events;

import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.text.Text;
import org.hydrogenhack.event.Event;

public class EventMessageReceive extends Event {
    private static final EventMessageReceive INSTANCE = new EventMessageReceive();

    private Text message;
    private MessageIndicator indicator;
    private boolean modified;
    public int id;

    public static EventMessageReceive get(Text message, MessageIndicator indicator, int id) {
        INSTANCE.setCancelled(false);
        INSTANCE.message = message;
        INSTANCE.indicator = indicator;
        INSTANCE.modified = false;
        INSTANCE.id = id;
        return INSTANCE;
    }

    public Text getMessage() {
        return message;
    }

    public MessageIndicator getIndicator() {
        return indicator;
    }

    public void setMessage(Text message) {
        this.message = message;
        this.modified = true;
    }

    public void setIndicator(MessageIndicator indicator) {
        this.indicator = indicator;
        this.modified = true;
    }

    public boolean isModified() {
        return modified;
    }
}
