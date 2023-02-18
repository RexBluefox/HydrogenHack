package org.hydrogenhack.command.commands;

import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.util.math.Vec3d;
import org.hydrogenhack.command.Command;
import org.hydrogenhack.command.CommandCategory;
import org.hydrogenhack.util.BleachLogger;
import org.hydrogenhack.util.PacketHelper;

public class CmdTP extends Command {
    public CmdTP() {
        super("hhclip", "Teleport", "tp <packets> <blocks>", CommandCategory.MISC, "teleport");
    }

    @Override
    public void onCommand(String alias, String[] args) throws Exception {
        int packets = Integer.parseInt(args[0]);
        double blocks = Double.parseDouble(args[1]);
        Vec3d forward = Vec3d.fromPolar(0, mc.player.getYaw()).normalize();
        for (int i = 0;i < packets;i++) {
            mc.player.setPosition(mc.player.getX() + forward.x * blocks/packets, mc.player.getY(), mc.player.getZ() + forward.z * blocks/packets);
            //PacketHelper.sendPosition(new Vec3d(mc.player.getX() + forward.x * blocks/packets, mc.player.getY(), mc.player.getZ() + forward.z * blocks/packets));
        }
        BleachLogger.info(String.format("Moved %s Steps with %s blocks per step", String.valueOf(packets),String.valueOf(blocks/packets)));
    }
}
