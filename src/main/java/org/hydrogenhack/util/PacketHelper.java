package org.hydrogenhack.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import org.hydrogenhack.HydrogenHack;
import org.hydrogenhack.mixin.InvokerClientConnection;

public class PacketHelper {
    public static void sendPosition(Vec3d pos){
        MinecraftClient client = HydrogenHack.mc;
        InvokerClientConnection conn = (InvokerClientConnection) client.player.networkHandler.getConnection();
        Packet packet = new PlayerMoveC2SPacket.PositionAndOnGround(pos.getX(), pos.getY(), pos.getZ(), false);
        conn.sendIm(packet,null);
    }
    public static void sendPacketImmediatly(Packet packet){
        MinecraftClient client = HydrogenHack.mc;
        InvokerClientConnection conn = (InvokerClientConnection) client.player.networkHandler.getConnection();
        conn.sendIm(packet,null);
    }
}
