package org.hydrogenhack.module.mods;


import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.network.message.SentMessage;
import net.minecraft.network.packet.s2c.play.VehicleMoveS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.profiling.jfr.event.PacketEvent;
import org.hydrogenhack.event.events.EventBoatMove;
import org.hydrogenhack.event.events.EventTick;
import org.hydrogenhack.eventbus.BleachSubscribe;
import org.hydrogenhack.module.Module;
import org.hydrogenhack.module.ModuleCategory;
import org.hydrogenhack.setting.module.ModuleSetting;
import org.hydrogenhack.event.events.EventPacket;
import org.hydrogenhack.setting.module.SettingSlider;
import org.hydrogenhack.setting.module.SettingToggle;
import org.hydrogenhack.util.BleachLogger;
import org.hydrogenhack.util.IVec3d;
import org.hydrogenhack.util.PlayerUtils;
import org.joml.Vector3d;

public class BoatFly extends Module {
    public BoatEntity currentBoat;
    public BoatFly() {
        super("BoatFly", KEY_UNBOUND, ModuleCategory.BLUEFOX, "Pls don't recreate 911",
                new SettingToggle("Cancel Server Packets",false),
                new SettingSlider("speed",0,100,10,1),
                new SettingSlider("vertical-speed",0,20,6,1),
                new SettingSlider("fall-speed",0,10,0.1,1)
                );
    }
    @BleachSubscribe
    public void onBoatMove(EventBoatMove event){
        currentBoat = event.boat;
        double verticalSpeed = this.getSetting(2).asSlider().getValue();
        double speed = this.getSetting(1).asSlider().getValue();
        if (event.boat == null) {
            BleachLogger.logger.error("Boat was null");
            return;
        }
        double fallSpeed = this.getSetting(3).asSlider().getValue();
        if (event.boat.getPrimaryPassenger() != mc.player) return;
        event.boat.setYaw(mc.player.getYaw());

        // Horizontal movement
        Vec3d vel = PlayerUtils.getHorizontalVelocity(speed);
        double velX = vel.getX();
        double velY = 0;
        double velZ = vel.getZ();

        // Vertical movement
        if (mc.options.jumpKey.isPressed()) velY += verticalSpeed / 20;
        if (mc.options.sprintKey.isPressed()) velY -= verticalSpeed / 20;
        else velY -= fallSpeed / 20;

        // Apply velocity
        ((IVec3d) event.boat.getVelocity()).set(velX, velY, velZ);
    };
    @BleachSubscribe
    public void onTick(EventTick event){
        Vec3d antiKickVel = Vec3d.ZERO;
        Vector3d antiKickVel2 = new Vector3d(0,0,0);
        if (mc.player.age % 40 == 0) {
            if (mc.world.getBlockState(new BlockPos(new BlockPos(currentBoat.getPos().add(0, 0.15, 0)))).getMaterial().isReplaceable()) {
                antiKickVel = antiKickVel.add(0, 0.15, 0);
                antiKickVel2 = antiKickVel2.add(0,0.15,0);
            }
        } else if (mc.player.age % 20 == 0) {
            if (mc.world.getBlockState(new BlockPos(new BlockPos(currentBoat.getPos().add(0, -0.15, 0)))).getMaterial().isReplaceable()) {
                antiKickVel = antiKickVel.add(0, -0.15, 0);
                antiKickVel2 = antiKickVel2.add(0,-0.15,0);
            }
        }
        ((IVec3d) currentBoat.getVelocity()).set(antiKickVel2);
       // currentBoat.setVelocity(antiKickVel);

    }

    @BleachSubscribe
    public void readPacket(EventPacket.Read event) {
        boolean cancelServerPackets = this.getSetting(0).asToggle().getState();
        if (event.getPacket() instanceof VehicleMoveS2CPacket && cancelServerPackets) {
            event.setCancelled(true);
        }
    }
}
