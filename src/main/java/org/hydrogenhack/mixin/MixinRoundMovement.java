package org.hydrogenhack.mixin;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.hydrogenhack.module.ModuleManager;
import org.hydrogenhack.module.mods.IamBot;
import org.hydrogenhack.module.mods.NoRender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static net.minecraft.util.math.MathHelper.sign;

@Mixin(PlayerMoveC2SPacket.class)
public class MixinRoundMovement {
    private static double round_fix(double value){
        double temp = (double) (Math.round(value*100))/100;
        return (double) Math.nextAfter(temp, temp + sign(value));
    }
    @ModifyVariable(method="<init>(DDDFFZZZ)V",at = @At("HEAD"),ordinal = 0)
    private static double RoundX(double x){
        //int places = (int) Modules.get().get(IamBot.class).settings.get("round-value").get();
        if (!ModuleManager.getModule(IamBot.class).isEnabled()) return x;
        //ChatUtils.info("Will round X to " + places + "places");
        //double newX = round(x,places);
        double newX = round_fix(x);
        return newX;
    }
    @ModifyVariable(method="<init>(DDDFFZZZ)V",at = @At("HEAD"),ordinal = 2)
    private static double RoundZ(double z){
        //Double places = ModuleManager.getModule(IamBot.class).getSetting(1).asSlider().getValue();
        if (!ModuleManager.getModule(IamBot.class).isEnabled()) return z;
        //ChatUtils.info("Will round Z to " + places + "places");
        //double newZ = round(z,places);
        double newZ = round_fix(z);
        return newZ;
    }
}
