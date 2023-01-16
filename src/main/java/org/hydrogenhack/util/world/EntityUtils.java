/*
 * This file is part of the BleachHack distribution (https://github.com/BleachDev/BleachHack/).
 * Copyright (c) 2021 Bleach and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package org.hydrogenhack.util.world;

import net.minecraft.world.chunk.EmptyChunk;
import org.hydrogenhack.HydrogenHack;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import org.hydrogenhack.util.MovementUtil;

import static org.hydrogenhack.HydrogenHack.mc;

public class EntityUtils {

	public static boolean isAnimal(Entity e) {
		return e instanceof PassiveEntity
				|| e instanceof AmbientEntity
				|| e instanceof WaterCreatureEntity
				|| e instanceof IronGolemEntity
				|| e instanceof SnowGolemEntity;
	}

	public static boolean isMob(Entity e) {
		return e instanceof Monster;
	}

	public static boolean isPlayer(Entity e) {
		return e instanceof PlayerEntity;
	}

	public static boolean isOtherServerPlayer(Entity e) {
		return e instanceof PlayerEntity
				&& e != MinecraftClient.getInstance().player
				&& !(e instanceof PlayerCopyEntity);
	}

	public static boolean isAttackable(Entity e, boolean ignoreFriends) {
		return (e instanceof LivingEntity || e instanceof ShulkerBulletEntity || e instanceof AbstractFireballEntity)
				&& e.isAlive()
				&& e != MinecraftClient.getInstance().player
				&& !e.isConnectedThroughVehicle(MinecraftClient.getInstance().player)
				&& !(e instanceof PlayerCopyEntity)
				&& (!ignoreFriends || !HydrogenHack.friendMang.has(e));
	}

	public static void steerEntity(Entity entity, float speed, boolean antiStuck){
		double yawRad = MovementUtil.calcMoveYaw();

		double motionX = -Math.sin(yawRad) * speed;
		double motionZ = Math.cos(yawRad) * speed;

		if (MovementUtil.isInputting() && !isBorderingChunk(entity,motionX,motionZ,antiStuck)) {
			setEntityMotionX(entity,motionX);
			setEntityMotionZ(entity, motionZ);
		} else {
			setEntityMotionX(entity, 0.0);
			setEntityMotionZ(entity,0.0);
		}
	}

	public static void setEntityMotionX(Entity entity, double motionX){
		entity.setVelocity(motionX,entity.getVelocity().y,entity.getVelocity().z);
	}
	public static void setEntityMotionY(Entity entity, double motionY){
		entity.setVelocity(entity.getVelocity().x,motionY,entity.getVelocity().z);
	}
	public static void setEntityMotionZ(Entity entity, double motionZ){
		entity.setVelocity(entity.getVelocity().x,entity.getVelocity().y,motionZ);
	}
	private static boolean isBorderingChunk(Entity entity, double motionX, double motionZ, boolean antiStuck) {
		int x = (int)(entity.getX() + motionX) >> 4;
		int z = (int)(entity.getZ() + motionZ) >> 4;
		return antiStuck && mc.world.getChunk(x, z) instanceof EmptyChunk;
	}
}
