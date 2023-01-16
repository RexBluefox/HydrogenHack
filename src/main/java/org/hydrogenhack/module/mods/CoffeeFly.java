package org.hydrogenhack.module.mods;

import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import org.hydrogenhack.HydrogenHack;
import org.hydrogenhack.event.events.EventTick;
import org.hydrogenhack.eventbus.BleachSubscribe;
import org.hydrogenhack.module.Module;
import org.hydrogenhack.module.ModuleCategory;
import org.hydrogenhack.setting.module.ModuleSetting;
import org.hydrogenhack.setting.module.SettingKey;
import org.hydrogenhack.util.BleachLogger;
import org.lwjgl.glfw.GLFW;

import java.security.Key;
import java.util.Objects;

public class CoffeeFly extends Module {
    public CoffeeFly() {
        super("CoffeeFly", KEY_UNBOUND,ModuleCategory.BLUEFOX,"Flight with a cup of coffee",
                new SettingKey(GLFW.GLFW_KEY_LEFT_ALT));
    }
    Integer downKEy = getSetting(0).asBind().getValue();
    final KeyBinding down = new KeyBinding("", downKEy, "");
    Entity lastRide = null;
    @BleachSubscribe
    public void onTick(EventTick event){
        if (mc.player == null || mc.getNetworkHandler() == null) {
            return;
        }
        Entity vehicle = mc.player.getVehicle();
        if (vehicle == null) {
            return;
        }
        lastRide = vehicle;
        vehicle.setNoGravity(true);
        if (vehicle instanceof MobEntity) {
            ((MobEntity) vehicle).setAiDisabled(true);
        }
        GameOptions go = mc.options;
        float y = Objects.requireNonNull(mc.player).getYaw();
        int mx = 0, my = 0, mz = 0;
        if (go.jumpKey.isPressed()) {
            my++;
        }
        if (go.backKey.isPressed()) {
            mz++;
        }
        if (go.leftKey.isPressed()) {
            mx--;
        }
        if (go.rightKey.isPressed()) {
            mx++;
        }
        if (down.isPressed()) {
            my--;
        }
        if (go.forwardKey.isPressed()) {
            mz--;
        }
        double ts = 1;
        double s = Math.sin(Math.toRadians(y));
        double c = Math.cos(Math.toRadians(y));
        double nx = ts * mz * s;
        double nz = ts * mz * -c;
        double ny = ts * my;
        nx += ts * mx * -c;
        nz += ts * mx * -s;
        Vec3d nv3 = new Vec3d(nx, ny - 0.1, nz);
        vehicle.setVelocity(nv3);
        vehicle.setYaw(mc.player.getYaw());
        VehicleMoveC2SPacket p = new VehicleMoveC2SPacket(vehicle);
        Objects.requireNonNull(mc.getNetworkHandler()).sendPacket(p);
    }
    @Override
    public void onEnable(boolean inWorld) {
        BleachLogger.info("Press left alt to descend");
    }

    @Override
    public void onDisable(boolean inWorld) {
        if (lastRide != null) {
            lastRide.setNoGravity(false);
        }
    }
}
