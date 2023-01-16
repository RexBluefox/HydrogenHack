package org.hydrogenhack.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import org.hydrogenhack.module.ModuleManager;
import org.hydrogenhack.module.mods.TextureRotation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(AbstractBlock.class)
public abstract class MixinTextureRotation {

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
    @Inject(method = "getRenderingSeed", at = @At("RETURN"), cancellable = true)
    private void getRenderingSeed(BlockState state, BlockPos pos, CallbackInfoReturnable<Long> cir) {
        TextureRotation mod = ModuleManager.getModule(TextureRotation.class);
        int mode = mod.getSetting(0).asMode().getMode();
        int rotateValue = mod.getSetting(1).asSlider().getValueInt();
        if (mod.isEnabled()) {
            if (mode == 0) {
                long l = (long) (pos.getX() * getRandomNumber(0, 3129871)) ^ (long) pos.getZ() * (long) getRandomNumber(0, 116129781) ^ (long) pos.getY();
                l = l * l * (long) getRandomNumber(0, 42317861) + l * 11L;
                cir.setReturnValue(l >> 16);
            } else if (mode == 1){
                long l = (long) (pos.getX() * rotateValue) ^ (long) pos.getZ() * (long) rotateValue ^ (long) pos.getY();
                l = l * l * (long) rotateValue + l * 11L;
                cir.setReturnValue(l >> 16);
            }
        }
        else
        {
            long l = (long)(pos.getX() * 3129871L) ^ (long)pos.getZ() * 116129781L ^ (long)pos.getY();
            l = l * l * 42317861L + l * 11L;
            cir.setReturnValue(l >> 16);
        }
    }
}