package org.hydrogenhack.module.mods;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.text.Text;
import org.hydrogenhack.event.events.EventPacket;
import org.hydrogenhack.eventbus.BleachSubscribe;
import org.hydrogenhack.gui.ChatFilterScreen;
import org.hydrogenhack.gui.NotebotScreen;
import org.hydrogenhack.module.Module;
import org.hydrogenhack.module.ModuleCategory;
import org.hydrogenhack.setting.module.ModuleSetting;
import org.hydrogenhack.setting.module.SettingBlockList;
import org.hydrogenhack.setting.module.SettingList;
import org.hydrogenhack.util.BleachQueue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatFilter extends Module {
    private String[] filters = new String[100000];
    public ChatFilter() {
        super("Chat Filter", KEY_UNBOUND, ModuleCategory.BLUEFOX, "Filters chat messages");
    }

    @BleachSubscribe
    public void onMessage(EventPacket packet){
        if (packet.getPacket().equals(ChatMessageS2CPacket.class)) {
            ChatMessageS2CPacket chatPacket = (ChatMessageS2CPacket) packet.getPacket();
            String message = chatPacket.message().getContent().getString();
            boolean matchFound = false;
            for (int i = 0; i < filters.length; i++) {
                Pattern pattern = Pattern.compile(filters[i], Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(message);
                matchFound = matcher.find();

            }
            if (matchFound) {
                setPacket(packet);
            }
        }
    }

    @Override
    public void onEnable(boolean inWorld) {
        filters[0] = "^EnderKill98 aquired a Club Mate";
        //BleachQueue.add(() -> mc.setScreen(new ChatFilterScreen()));
        super.onEnable(inWorld);
    }

    private void setPacket(EventPacket packet){
        ByteBuf buffer = Unpooled.buffer(0, 0);
        packet.setPacket(new ChatMessageS2CPacket(new PacketByteBuf(buffer)));
    }
}
