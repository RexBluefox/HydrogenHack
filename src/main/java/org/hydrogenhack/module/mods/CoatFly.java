package org.hydrogenhack.module.mods;

import net.minecraft.client.option.Perspective;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.BoatPaddleStateC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.EntitiesDestroyS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityPassengersSetS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.s2c.play.VehicleMoveS2CPacket;
import net.minecraft.util.Hand;
import org.hydrogenhack.event.events.EventPacket;
import org.hydrogenhack.event.events.EventSendMovementPackets;
import org.hydrogenhack.eventbus.BleachSubscribe;
import org.hydrogenhack.module.Module;
import org.hydrogenhack.module.ModuleCategory;
import org.hydrogenhack.setting.module.ModuleSetting;
import org.hydrogenhack.setting.module.SettingSlider;
import org.hydrogenhack.setting.module.SettingToggle;
import org.hydrogenhack.util.PlayerUtils;
import org.hydrogenhack.util.world.EntityUtils;
import java.util.Arrays;

public class CoatFly extends Module {


    public CoatFly() {
        super("CoatFly",KEY_UNBOUND,ModuleCategory.BLUEFOX,"Coat not Boat Fly",
                new SettingSlider("Speed",0,100,10,1),
                new SettingSlider("Up Speed",0,100,10,1),
                new SettingSlider("Glide Speed",0,100,10,1),
                new SettingToggle("Anti Stuck",true),
                new SettingToggle("Remount",false),
                new SettingToggle("Anti Force Look",true),
                new SettingToggle("Force Interact",true),
                new SettingToggle("Teleport Spoof",false),
                new SettingToggle("Cancel Player Packets",false),
                new SettingToggle("Anti Desync",false),
                new SettingSlider("Boat Opacity",0,1,1,1),
                new SettingSlider("Boat Scale",0.05,1.5,1,2));
    }
    private float speed = getSetting(0).asSlider().getValueFloat();
    private Double upSpeed = getSetting(1).asSlider().getValue();
    private Double glideSpeed = getSetting(2).asSlider().getValue();
    private boolean antiStuck = getSetting(3).asToggle().getState();
    private boolean remount = getSetting(4).asToggle().getState();
    private boolean antiForceLook = getSetting(5).asToggle().getState();
    private boolean forceInteract = getSetting(6).asToggle().getState();
    private boolean teleportSpoof = getSetting(7).asToggle().getState();
    private boolean cancelPlayer = getSetting(8).asToggle().getState();
    private boolean antiDesyc = getSetting(9).asToggle().getState();
    public Double opacity= getSetting(10).asSlider().getValue();
    public Double size = getSetting(11).asSlider().getValue();

    @Override
    public void onDisable(boolean inWorld) {
        if (antiDesyc){
            mc.player.networkHandler.sendPacket(new PlayerInputC2SPacket(0.0f,0.0f,false,true));
            mc.player.dismountVehicle();
        }
    }

    @BleachSubscribe
    public void onSend(EventPacket.Send event){
        Entity ridingEntity = mc.player.getVehicle();
        if (!(ridingEntity instanceof BoatEntity)|| !cancelPlayer) return;
        Packet<?> packet = event.getPacket();
        if (packet instanceof PlayerMoveC2SPacket || packet instanceof PlayerInputC2SPacket || packet instanceof BoatPaddleStateC2SPacket){
            if (packet instanceof PlayerInputC2SPacket && packet.equals(new PlayerInputC2SPacket(0.0f, 0.0f, false, true))) {
                return;
            } else {
                event.setCancelled(true);
            }
        }
    }

    @BleachSubscribe
    public void onRead(EventPacket.Read event){
        assert mc.player != null;
        ClientWorld world = mc.world;
        Entity ridingEntity = mc.player.getVehicle();
        Packet<?> packet = event.getPacket();
        if (packet instanceof EntityPassengersSetS2CPacket){
            if (remount) {
                EntityPassengersSetS2CPacket setPacket = (EntityPassengersSetS2CPacket) packet;
                Entity entity = world.getEntityById(setPacket.getId());
                if (entity != null) {
                    if (!setPacket.getPassengerIds().toString().contains(String.valueOf(mc.player.getId())) && ridingEntity.getId() == setPacket.getId()) {
                        if (teleportSpoof) event.setCancelled(true);
                        mc.interactionManager.interactEntity(mc.player, entity, Hand.OFF_HAND);
                    } else if (setPacket.getPassengerIds().length == 0 && setPacket.getPassengerIds().toString().contains(String.valueOf(mc.player.getId()))) {
                        if (antiForceLook){
                            entity.setYaw(mc.player.prevYaw);
                            entity.setPitch(mc.player.prevPitch);
                        }
                    }
                }
            }
        }
        if (packet instanceof PlayerPositionLookS2CPacket){
             if (antiForceLook) {
                 PlayerPositionLookS2CPacket lookPacket = (PlayerPositionLookS2CPacket) event.getPacket();
                 PlayerPositionLookS2CPacket newLookPacket = new PlayerPositionLookS2CPacket(mc.player.getX(), mc.player.getY(), mc.player.getZ(), mc.player.getYaw(), mc.player.getPitch(), lookPacket.getFlags(),lookPacket.getTeleportId(),lookPacket.shouldDismount());
                 event.setPacket(newLookPacket);
             }
        }
        if (packet instanceof EntitiesDestroyS2CPacket){}
        if (packet instanceof VehicleMoveS2CPacket){
            if(forceInteract) event.setCancelled(true);
        }
    }

    @BleachSubscribe
    public void onPlayerTravel(EventSendMovementPackets event){
        assert mc.player != null;
        Entity ridingEntity = mc.player.getVehicle();
        if (!(ridingEntity instanceof BoatEntity)) return;
        ridingEntity.setYaw(mc.player.getYaw());
        ((BoatEntity) ridingEntity).setInputs(false,false,false,false);
        ridingEntity.setNoGravity(true);
        ridingEntity.setVelocity(ridingEntity.getVelocity().x,0.0,ridingEntity.getVelocity().z);
        if (glideSpeed > 0 && !mc.options.jumpKey.isPressed()) ridingEntity.setVelocity(ridingEntity.getVelocity().x,-glideSpeed,ridingEntity.getVelocity().z);
        if (mc.options.jumpKey.isPressed()) ridingEntity.setVelocity(ridingEntity.getVelocity().x,upSpeed,ridingEntity.getVelocity().z);
        if (mc.options.sneakKey.isPressed()) ridingEntity.setVelocity(ridingEntity.getVelocity().x,-upSpeed,ridingEntity.getVelocity().z);

        EntityUtils.steerEntity(ridingEntity,speed,antiStuck);

    }
    public boolean isBoatFlying(Entity entityIn){
        return isEnabled() && mc.player.getVehicle() == entityIn;
    }

    public boolean shouldModifyScale(Entity entityIn){
        return isBoatFlying(entityIn) && mc.options.getPerspective() == Perspective.FIRST_PERSON;
    }
}
