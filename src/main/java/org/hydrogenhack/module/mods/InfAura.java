package org.hydrogenhack.module.mods;

import baritone.BaritoneProvider;
import baritone.api.BaritoneAPI;
import com.google.common.collect.Streams;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.hydrogenhack.event.events.EventSwingHand;
import org.hydrogenhack.event.events.EventTick;
import org.hydrogenhack.event.events.EventWorldRender;
import org.hydrogenhack.eventbus.BleachSubscribe;
import org.hydrogenhack.module.Module;
import org.hydrogenhack.module.ModuleCategory;
import org.hydrogenhack.module.ModuleManager;
import org.hydrogenhack.setting.module.*;
import org.hydrogenhack.util.BaritoneUtils;
import org.hydrogenhack.util.BleachLogger;
import org.hydrogenhack.util.PacketHelper;
import org.hydrogenhack.util.render.Renderer;
import org.hydrogenhack.util.render.color.QuadColor;
import org.hydrogenhack.util.world.EntityUtils;
import org.hydrogenhack.util.world.WorldUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InfAura extends Module {
    private Entity lookingAt;
    public InfAura() {
        super("InfAura",KEY_UNBOUND,ModuleCategory.BLUEFOX,"Reach Infinitly",
                new SettingMode("Sort", "Angle", "Distance").withDesc("How to sort targets."),//0
                new SettingToggle("Players", true).withDesc("Attacks Players."),//1
                new SettingToggle("Mobs", true).withDesc("Attacks Mobs."),//2
                new SettingToggle("Animals", false).withDesc("Attacks Animals."),//3
                new SettingToggle("ArmorStands", false).withDesc("Attacks Armor Stands."),//4
                new SettingToggle("Projectiles", false).withDesc("Attacks Shulker Bullets & Fireballs."),//5
                new SettingToggle("Triggerbot", false).withDesc("Only attacks the entity you're looking at."),//6
                new SettingToggle("MultiAura", false).withDesc("Atacks multiple entities at once.").withChildren(//7
                        new SettingSlider("Targets", 1, 20, 3, 0).withDesc("How many targets to attack at once.")),
                new SettingRotate(true).withDesc("Rotates when attackign entities."),//8
                new SettingToggle("Raycast", true).withDesc("Only attacks if you can see the target."),//9
                new SettingToggle("1.9 Delay", false).withDesc("Uses the 1.9+ delay between hits."),//10
                new SettingSlider("Range", 0, 100, 4.25, 2).withDesc("Attack range."),//11
                new SettingSlider("CPS", 0, 20, 8, 0).withDesc("Attack CPS if 1.9 delay is disabled."),//12
                new SettingToggle("Render",true).withDesc("highlight target entity").withChildren(//13
                        new SettingColor("Color",0,0,255),
                        new SettingToggle("Outline",false).withChildren(
                                new SettingSlider("Width",0,5,2,1),
                                new SettingColor("Outline Color",0,0,255),
                                new SettingSlider("Outline Opacity",0,255,50,0)
                        ),
                        new SettingSlider("Opacity",0,255,50,0)
                ));
    }
    @BleachSubscribe
    public void onSwing(EventSwingHand event) throws InterruptedException {
        if (!mc.player.isAlive()) return;
        if (!isEnabled()) return;
        BleachLogger.logger.info("Swung: " + ((event.getHand().equals(Hand.MAIN_HAND) ? "Main Hand" : "Off Hand") + " while looking at " + lookingAt.getDisplayName().getString()));
        Vec3d pos = lookingAt.getPos();
        Vec3d oldPos = mc.player.getPos();
        teleportFromTo(mc,oldPos, pos);
        PlayerInteractEntityC2SPacket attackPacket = PlayerInteractEntityC2SPacket.attack(lookingAt,false);
        PacketHelper.sendPacketImmediatly(attackPacket);
        teleportFromTo(mc,pos,oldPos);
        mc.player.setPosition(oldPos);
    }
    @BleachSubscribe
    public void onRender(EventWorldRender.Post event) {
        boolean shouldRender = getSetting(13).asToggle().getState();
        if (shouldRender) {
            if (lookingAt == null)return;

            int[] color = getSetting(13).asToggle().getChild(0).asColor().getRGBArray();

            SettingToggle outline = getSetting(13).asToggle().getChild(1).asToggle();
            int opacity = getSetting(13).asToggle().getChild(2).asSlider().getValueInt();
            if (outline.getState()){
                float width = outline.getChild(0).asSlider().getValueFloat();
                int[] outlineColor = outline.getChild(1).asColor().getRGBArray();
                int outlineOpacity = outline.getChild(2).asSlider().getValueInt();
                Renderer.drawBoxOutline(lookingAt.getBoundingBox(),QuadColor.single(outlineColor[0], outlineColor[1], outlineColor[2], outlineOpacity), width);
            }
            Renderer.drawBoxFill(lookingAt.getBoundingBox(), QuadColor.single(color[0], color[1], color[2], opacity));
        }
    }

    @BleachSubscribe
    public void onTick(EventTick event) {
        if (!mc.player.isAlive()) return;

        if(!isEnabled()) return;
        String text = "";
        List<Entity> entities = getEntities();
        for (Entity e: entities) {
            String entityName = e.getDisplayName().getString();
            text += "I see: " + entityName + ", Distance: " + e.distanceTo(mc.player);
            lookingAt = e;
            //BleachLogger.info("I see: " + e.getDisplayName().getString());
        }
        UI ui = ModuleManager.getModule(UI.class);
        ui.setInfAuraText(text);
        text = "";
    }
    public void onDisable(boolean inWorld) {
        UI ui = ModuleManager.getModule(UI.class);
        ui.setInfAuraText("");
    }


    private void teleportFromTo(MinecraftClient client,Vec3d fromPos,Vec3d toPos){
        double distancePerBlink = 8.5;
        double targetDistance = Math.ceil(fromPos.distanceTo(toPos)/distancePerBlink);
        BleachLogger.logger.info("Target Distance " + targetDistance);
        for (int i = 1; i<=targetDistance;i++){
            Vec3d tempPos = fromPos.lerp(toPos,i/targetDistance);
            BleachLogger.logger.info("Temp Pos " + tempPos.x + " " + tempPos.y+ " " + tempPos.z);
            PacketHelper.sendPosition(tempPos);
            if (i%4 == 0){
                try{
                    Thread.sleep((long)((1/20)*1000));
                    BleachLogger.logger.info("sleep");
                } catch (InterruptedException e){
                    BleachLogger.logger.error("sleep fail");
                }
            }
        }
    }
    private List<Entity> getEntities() {
        Stream<Entity> targets;

        if (getSetting(6).asToggle().getState()) {
            Optional<Entity> entity = DebugRenderer.getTargetedEntity(mc.player, 7);

            if (entity.isEmpty()) {
                return Collections.emptyList();
            }

            targets = Stream.of(entity.get());
        } else {
            targets = Streams.stream(mc.world.getEntities());
        }

        Comparator<Entity> comparator;

        if (getSetting(0).asMode().getMode() == 0) {
            comparator = Comparator.comparing(e -> {
                Vec3d center = e.getBoundingBox().getCenter();

                double diffX = center.x - mc.player.getX();
                double diffY = center.y - mc.player.getEyeY();
                double diffZ = center.z - mc.player.getZ();

                double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

                float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F;
                float pitch = (float) -Math.toDegrees(Math.atan2(diffY, diffXZ));

                return Math.abs(MathHelper.wrapDegrees(yaw - mc.player.getYaw())) + Math.abs(MathHelper.wrapDegrees(pitch - mc.player.getPitch()));
            });
        } else {
            comparator = Comparator.comparing(mc.player::distanceTo);
        }

        return targets
                .filter(e -> EntityUtils.isAttackable(e, true)
                        && mc.player.distanceTo(e) <= getSetting(11).asSlider().getValue()
                        && (mc.player.canSee(e) || !getSetting(9).asToggle().getState()))
                .filter(e -> (EntityUtils.isPlayer(e) && getSetting(1).asToggle().getState())
                        || (EntityUtils.isMob(e) && getSetting(2).asToggle().getState())
                        || (EntityUtils.isAnimal(e) && getSetting(3).asToggle().getState())
                        || (e instanceof ArmorStandEntity && getSetting(4).asToggle().getState())
                        || ((e instanceof ShulkerBulletEntity || e instanceof AbstractFireballEntity) && getSetting(5).asToggle().getState()))
                .sorted(comparator)
                .limit(getSetting(7).asToggle().getState() ? getSetting(7).asToggle().getChild(0).asSlider().getValueLong() : 1L)
                .collect(Collectors.toList());
    }
}
