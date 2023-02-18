package org.hydrogenhack.mixin;

import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Camera.class)
public interface AccessorCamera {
    @Accessor
    void setFocusedEntity(Entity focusedEntityNew);
}
