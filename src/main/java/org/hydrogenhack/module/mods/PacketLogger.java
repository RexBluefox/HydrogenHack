package org.hydrogenhack.module.mods;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.network.packet.s2c.play.UnlockRecipesS2CPacket;
import org.hydrogenhack.event.events.EventPacket;
import org.hydrogenhack.eventbus.BleachSubscribe;
import org.hydrogenhack.module.Module;
import org.hydrogenhack.module.ModuleCategory;
import org.hydrogenhack.setting.module.ModuleSetting;
import org.hydrogenhack.setting.module.SettingToggle;
import org.hydrogenhack.util.BleachLogger;
public class PacketLogger extends Module {
    public PacketLogger() {
        super("PacketLogger",KEY_UNBOUND,ModuleCategory.BLUEFOX,"Logs Packets to chat",
                new SettingToggle("Send",true),
                new SettingToggle("Read",true));
    }

    @BleachSubscribe
    public void onRead(EventPacket.Read event){
        if (!getSetting(1).asToggle().getState()) return;
        Packet<?> packet = event.getPacket();
        if (event.getPacket().toString().contains("Entit") || event.getPacket().toString().contains("Player")) return;
        String packetName = event.getPacket().toString();
        BleachLogger.info("< " + packetName);

//        if (packet instanceof InventoryS2CPacket invPacket){
//            BleachLogger.info("< InventoryS2CPacket"+invPacket.getContents().toString() + "\n" + invPacket.getRevision() + "\n" + invPacket.getCursorStack().toString() +"\n" + invPacket.getSyncId());
//        }
//        if (packet instanceof UnlockRecipesS2CPacket recipPacket){
//            BleachLogger.info("< " + recipPacket.getAction().toString() + "\n" + recipPacket.getOptions().toString() + "\n" + recipPacket.getRecipeIdsToChange().toString() +"\n"+recipPacket.getRecipeIdsToInit().toString());
//        }
         //UnlockRecipesS2CPacket recipPacket = new UnlockRecipesS2CPacket(new PacketByteBuf(null));
//
    }

    @BleachSubscribe
    public void onSend(EventPacket.Send event){
        if (!getSetting(0).asToggle().getState()) return;
        if (event.getPacket().toString().contains("Entit") || event.getPacket().toString().contains("Player")) return;
        String packetName = event.getPacket().toString();
        BleachLogger.info("> "+packetName);
        Packet<?> packet = event.getPacket();
//        if (packet instanceof InventoryS2CPacket invPacket){
//            BleachLogger.info("> InventoryS2CPacket"+invPacket.getContents().toString() + "\n" + invPacket.getRevision() + "\n" + invPacket.getCursorStack().toString() +"\n" + invPacket.getSyncId());
//        }
//        if (packet instanceof UnlockRecipesS2CPacket recipPacket){
//            BleachLogger.info("> " + recipPacket.getAction().toString() + "\n" + recipPacket.getOptions() + "\n" + recipPacket.getRecipeIdsToChange().toString() +"\n"+recipPacket.getRecipeIdsToInit().toString());
//        }
    }
}
