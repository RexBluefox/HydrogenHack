package org.hydrogenhack.mixin;

import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(VehicleMoveC2SPacket.class)
public interface AccessorVehicleMove {
    @Mutable
    @Accessor("x")
    public void setX(double x);

    @Mutable
    @Accessor("z")
    public void setZ(double z);
}
