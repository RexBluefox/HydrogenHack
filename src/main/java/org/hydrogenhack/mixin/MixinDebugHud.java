package org.hydrogenhack.mixin;

import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import com.mojang.blaze3d.platform.GlDebugInfo;
import com.sun.jna.platform.unix.aix.Perfstat;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import org.hydrogenhack.HydrogenHack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

@Mixin(DebugHud.class)
public class MixinDebugHud {

    @Inject(method = "getRightText",at=@At("RETURN"))
    public void censorBlockCoords(CallbackInfoReturnable<List<String>> cir) {
        //cir.get
//        long l = Runtime.getRuntime().maxMemory();
//        long m = Runtime.getRuntime().totalMemory();
//        long n = Runtime.getRuntime().freeMemory();
//        long o = m - n;
//
//        List<String> list = Lists.newArrayList(new String[]{String.format(Locale.ROOT, "Java: %s %dbit", System.getProperty("java.version"), this.client.is64Bit() ? 64 : 32), String.format(Locale.ROOT, "Mem: % 2d%% %03d/%03dMB", o * 100L / l, toMiB(o), toMiB(l)), String.format(Locale.ROOT, "Allocation rate: %03dMB /s", toMiB(this.allocationRateCalculator.get(o))), String.format(Locale.ROOT, "Allocated: % 2d%% %03dMB", m * 100L / l, toMiB(m)), "", String.format(Locale.ROOT, "CPU: %s", GlDebugInfo.getCpuInfo()), "", String.format(Locale.ROOT, "Display: %dx%d (%s)", MinecraftClient.getInstance().getWindow().getFramebufferWidth(), MinecraftClient.getInstance().getWindow().getFramebufferHeight(), GlDebugInfo.getVendor()), GlDebugInfo.getRenderer(), GlDebugInfo.getVersion()});
//        Perfstat.perfstat_protocol_t.AnonymousStructNFS mc;
//        if (HydrogenHack.mc.hasReducedDebugInfo()) {
//            return list;
//        } else {
//            BlockPos blockPos;
//            UnmodifiableIterator var12;
//            Map.Entry entry;
//            Stream var10000;
//            Formatting var10001;
//            if (this.blockHit.getType() == net.minecraft.util.hit.HitResult.Type.BLOCK) {
//                blockPos = ((BlockHitResult)this.blockHit).getBlockPos();
//                BlockState blockState = this.client.world.getBlockState(blockPos);
//                list.add("");
//                var10001 = Formatting.UNDERLINE;
//                list.add("" + var10001 + "Targeted Block: " + blockPos.getX() + ", " + blockPos.getY() + ", " + blockPos.getZ());
//                list.add(String.valueOf(Registry.BLOCK.getId(blockState.getBlock())));
//                var12 = blockState.getEntries().entrySet().iterator();
//
//                while(var12.hasNext()) {
//                    entry = (Map.Entry)var12.next();
//                    list.add(this.propertyToString(entry));
//                }
//
//                var10000 = blockState.streamTags().map((tag) -> {
//                    return "#" + tag.id();
//                });
//                Objects.requireNonNull(list);
//                var10000.forEach(list::add);
//            }
//
//            if (this.fluidHit.getType() == net.minecraft.util.hit.HitResult.Type.BLOCK) {
//                blockPos = ((BlockHitResult)this.fluidHit).getBlockPos();
//                FluidState fluidState = this.client.world.getFluidState(blockPos);
//                list.add("");
//                var10001 = Formatting.UNDERLINE;
//                list.add("" + var10001 + "Targeted Fluid: " + blockPos.getX() + ", " + blockPos.getY() + ", " + blockPos.getZ());
//                list.add(String.valueOf(Registry.FLUID.getId(fluidState.getFluid())));
//                var12 = fluidState.getEntries().entrySet().iterator();
//
//                while(var12.hasNext()) {
//                    entry = (Map.Entry)var12.next();
//                    list.add(this.propertyToString(entry));
//                }
//
//                var10000 = fluidState.streamTags().map((tag) -> {
//                    return "#" + tag.id();
//                });
//                Objects.requireNonNull(list);
//                var10000.forEach(list::add);
//            }
//
//            Entity entity = this.client.targetedEntity;
//            if (entity != null) {
//                list.add("");
//                list.add(Formatting.UNDERLINE + "Targeted Entity");
//                list.add(String.valueOf(Registry.ENTITY_TYPE.getId(entity.getType())));
//            }
//
//            return list;
//        }
    }
}
