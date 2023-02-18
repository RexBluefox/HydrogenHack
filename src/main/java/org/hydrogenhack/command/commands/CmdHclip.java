package org.hydrogenhack.command.commands;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;
import org.hydrogenhack.command.Command;
import org.hydrogenhack.command.CommandCategory;
import org.hydrogenhack.util.BleachLogger;
import org.hydrogenhack.util.PacketHelper;

public class CmdHclip extends Command {
    public CmdHclip() {
        super("hclip", "Teleport", "hclip <x> <y> <z>", CommandCategory.MISC,"tp");
    }

    @Override
    public void onCommand(String alias, String[] args) throws Exception {
//        for (int i = 0;i < args.length; i++){
//            BleachLogger.info(args[i]);
//        }
        //BleachLogger.info(String.valueOf(args));
        //String[] location = args[0].split(" ");
        //BleachLogger.info(location);
        double x = Double.parseDouble(args[0]);
        double y = Double.parseDouble(args[1]);
        double z = Double.parseDouble(args[2]);
        double oldY = mc.player.getY();
        //teleportFromTo(mc,mc.player.getPos(), new Vec3d(mc.player.getX(), 255, mc.player.getZ()));
        //teleportFromTo(mc,mc.player.getPos(), new Vec3d(x, 255, z));
        mc.player.noClip = true;
        teleportFromTo(mc,mc.player.getPos(), new Vec3d(x, y, z));
        mc.player.setPosition(new Vec3d(x, y, z));
        //mc.player.noClip = false;
        BleachLogger.info("Tried going to " + x + " " + y + " " + z);

    }
    private void teleportFromTo(MinecraftClient client, Vec3d fromPos, Vec3d toPos){
        double distancePerBlink = 8.5;
        double targetDistance = Math.ceil(fromPos.distanceTo(toPos)/distancePerBlink);
        for (int i = 1; i<=targetDistance;i++){
            Vec3d tempPos = fromPos.lerp(toPos,i/targetDistance);
            PacketHelper.sendPosition(tempPos);
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
