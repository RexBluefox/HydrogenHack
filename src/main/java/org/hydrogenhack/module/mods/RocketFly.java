package org.hydrogenhack.module.mods;

import net.minecraft.entity.Entity;
import org.hydrogenhack.event.events.EventTick;
import org.hydrogenhack.eventbus.BleachSubscribe;
import org.hydrogenhack.module.Module;
import org.hydrogenhack.module.ModuleCategory;
import org.hydrogenhack.setting.module.ModuleSetting;
import org.hydrogenhack.util.PacketHelper;

public class RocketFly extends Module {
    public RocketFly() {
        super("RocketFly",KEY_UNBOUND,ModuleCategory.BLUEFOX,"SpaceX 2.0");
    }
    private int tickCounter = 1;
    @BleachSubscribe
    public void onTick(EventTick event){
        if (mc.options.jumpKey.isPressed()) {
            if (mc.player.isRiding()) {
                Entity ridingEntity = mc.player.getRootVehicle();
                if (tickCounter % 20 == 0) {
                    //ridingEntity.setPosition(ridingEntity.getPos().subtract(0.0, 0.0433D, 0.0));
                    ridingEntity.setVelocity(ridingEntity.getVelocity().subtract(0.0,0.0433D,0.0));
                    tickCounter++;
                } else {
                    //ridingEntity.setPosition(ridingEntity.getPos().add(0,10,0));
                    ridingEntity.setVelocity(ridingEntity.getVelocity().add(0.0,10,0.0));
                    //mc.player.setPosition(mc.player.getPos().add(0, 10, 0));
                    tickCounter++;
                }
            } else {
                if (tickCounter % 20 == 0) {
                    //mc.player.setPosition(mc.player.getPos().add(0,10,0));
                    //PacketHelper.sendPosition(mc.player.getPos().add(0,10,0));
                    PacketHelper.sendPosition(mc.player.getPos().subtract(0.0, 0.0433D, 0.0));
                    tickCounter++;
                } else {
                    //PacketHelper.sendPosition(mc.player.getPos().add(0,10,0));
                    mc.player.setPosition(mc.player.getPos().add(0, 10, 0));
                    //PacketHelper.sendPosition(mc.player.getPos().subtract(0.0,0.0433D,0.0));
                    tickCounter++;
                }
            }
        }
        if (mc.options.sneakKey.isPressed()){
            if (tickCounter % 20 == 0) {
                //mc.player.setPosition(mc.player.getPos().add(0,10,0));
                //PacketHelper.sendPosition(mc.player.getPos().add(0,10,0));
                PacketHelper.sendPosition(mc.player.getPos().subtract(0.0, 0.0433D, 0.0));
                tickCounter++;
            } else {
                //PacketHelper.sendPosition(mc.player.getPos().add(0,10,0));
                mc.player.setPosition(mc.player.getPos().subtract(0, 10, 0));
                //PacketHelper.sendPosition(mc.player.getPos().subtract(0.0,0.0433D,0.0));
                tickCounter++;
            }
        }
    }

}
