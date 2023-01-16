package org.hydrogenhack.mixin;

import net.minecraft.entity.vehicle.BoatEntity;
import org.hydrogenhack.HydrogenHack;
import org.hydrogenhack.event.events.EventBoatMove;
import org.hydrogenhack.module.ModuleManager;
import org.hydrogenhack.module.mods.BoatFly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BoatEntity.class)
public class MixinBoatEntity {
    @Shadow private boolean pressingLeft;

    @Shadow
    private boolean pressingRight;
    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/vehicle/BoatEntity;move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V"), cancellable = true)
    private void onTickInvokeMove(CallbackInfo info) {
        EventBoatMove event = new EventBoatMove();
        //HydrogenHack.eventBus.post(EventBoatMove.get((BoatEntity) (Object) this));
        HydrogenHack.eventBus.post(EventBoatMove.get((BoatEntity) (Object) this));
//        if (event.isCancelled()) {
//            info.cancel();
//        }
    }
    @Redirect(method = "updatePaddles", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/vehicle/BoatEntity;pressingLeft:Z"))
    private boolean onUpdatePaddlesPressingLeft(BoatEntity boat) {
        if (ModuleManager.getModule(BoatFly.class).isEnabled()) return false;
        return pressingLeft;
    }

    @Redirect(method = "updatePaddles", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/vehicle/BoatEntity;pressingRight:Z"))
    private boolean onUpdatePaddlesPressingRight(BoatEntity boat) {
        if (ModuleManager.getModule(BoatFly.class).isEnabled()) return false;
        return pressingRight;
    }
}
