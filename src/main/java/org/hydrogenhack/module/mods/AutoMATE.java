package org.hydrogenhack.module.mods;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import org.hydrogenhack.module.Module;
import org.hydrogenhack.module.ModuleCategory;
import org.hydrogenhack.setting.module.ModuleSetting;
import org.hydrogenhack.util.BleachLogger;
import org.hydrogenhack.util.PacketHelper;

import java.lang.reflect.Executable;

public class AutoMATE extends Module {
    private final Vec3d mateCoords = new Vec3d(1332, 79, 1294);
    public static double MAX_DELTA = 0.05;
    public static ClientPlayNetworkHandler networkHandler;

    public AutoMATE() {
        super("AutoMATE",KEY_UNBOUND, ModuleCategory.BLUEFOX, "Automaticly farms Clubmate");
    }

    @Override
    public void onEnable(boolean inWorld) {
        networkHandler = mc.getNetworkHandler();
//        try {
//            teleportFromTo(mc,mc.player.getPos(),mateCoords);
//        } catch (Exception e) {
//            BleachLogger.error(e.getMessage());
//        }
        teleportFromTo(mc,mc.player.getPos(),mateCoords);
        super.onEnable(inWorld);
    }

    private void teleportFromTo(MinecraftClient client, Vec3d fromPos, Vec3d toPos){
        double distancePerBlink = MAX_DELTA;
        double targetDistance = Math.ceil(fromPos.distanceTo(toPos)/distancePerBlink);
        BleachLogger.logger.info("Target Distance " + targetDistance);
        for (int i = 1; i<=targetDistance;i++){
            Vec3d tempPos = fromPos.lerp(toPos,i/targetDistance);
            BleachLogger.logger.info("Temp Pos " + tempPos.x + " " + tempPos.y+ " " + tempPos.z);
            PacketHelper.sendPosition(tempPos);
//            PlayerMoveC2SPacket.Full farPacket = new PlayerMoveC2SPacket.Full(mc.player.getX() + 1337.0, mc.player.getY() + 1337.0,
//                    mc.player.getZ() + 1337.0, mc.player.getYaw(), mc.player.getPitch(), mc.player.isOnGround());
//            networkHandler.getConnection().send(farPacket);
            if (i%4 == 0){
                try{
                    Thread.sleep((long)((1/20)*1000));
                    BleachLogger.logger.info("sleep");
                } catch (InterruptedException e){
                    BleachLogger.logger.error("sleep fail");
                }
            }
        }
    }
}
