/*
 * This file is part of the BleachHack distribution (https://github.com/BleachDev/BleachHack/).
 * Copyright (c) 2021 Bleach and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package org.hydrogenhack.setting.module;

import com.mojang.blaze3d.systems.RenderSystem;
//import net.minecraft.Entity.Entity;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.*;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.hydrogenhack.setting.SettingDataHandlers;
import org.hydrogenhack.util.BleachLogger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SettingEntityTypeList extends SettingList<EntityType> {

	public SettingEntityTypeList(String text, String windowText, EntityType... defaultEntityTypes) {
		this(text, windowText, null, defaultEntityTypes);
	}

	public SettingEntityTypeList(String text, String windowText, Predicate<EntityType> filter, EntityType... defaultEntityTypes) {
		super(text, windowText, SettingDataHandlers.ENTITY_TYPE, getAllEntitys(filter), defaultEntityTypes);
	}

	private static Collection<EntityType> getAllEntitys(Predicate<EntityType> filter) {
		return filter == null
				? Registry.ENTITY_TYPE.stream().collect(Collectors.toList())
						: Registry.ENTITY_TYPE.stream().filter(filter).collect(Collectors.toList());
	}

	@Override
	public void renderItem(MinecraftClient mc, MatrixStack matrices, EntityType type, int x, int y, int w, int h) {
		Item item = SpawnEggItem.forEntity(type);
		if (item == null || item.asItem() == Items.AIR) {
			//BleachLogger.logger.info("Didn't find " + type.getName().getString());
			try {
				Item newitem = EntityToItem(type);
				if (newitem.equals(Items.AIR)){
					super.renderItem(mc, matrices, type, x, y, w, h);

					return;
				}
				//BleachLogger.logger.info(newitem.getName().getString());
				RenderSystem.getModelViewStack().push();

				float scale = (h - 2) / 16f;
				float offset = 1f / scale;
				RenderSystem.getModelViewStack().scale(scale, scale, 1f);


				mc.getItemRenderer().renderInGui(new ItemStack(newitem.asItem()), (int) ((x + 1) * offset), (int) ((y + 1) * offset));

				RenderSystem.getModelViewStack().pop();
				RenderSystem.applyModelViewMatrix();
				return;
			} catch (Exception e) {
				BleachLogger.logger.error(type.getName().getSiblings() + ": " + e.getMessage());
				super.renderItem(mc, matrices, type, x, y, w, h);
			}
		} else {
			RenderSystem.getModelViewStack().push();

			float scale = (h - 2) / 16f;
			float offset = 1f / scale;
			RenderSystem.getModelViewStack().scale(scale, scale, 1f);

			mc.getItemRenderer().renderInGui(new ItemStack(item.asItem()), (int) ((x + 1) * offset), (int) ((y + 1) * offset));

			RenderSystem.getModelViewStack().pop();
			RenderSystem.applyModelViewMatrix();
		}
	}

	

	@Override
	public Text getName(EntityType type) {
		return type.getName();
	}

	public Item EntityToItem(EntityType entity){
		Map<Identifier,Identifier> otherEntities = otherEntites();
		Identifier reg = entity.getRegistryEntry().registryKey().getValue();
		Item item = Registry.ITEM.get(reg).asItem();
		return item != Items.AIR ? item : Registry.ITEM.get(otherEntities.get(reg)).asItem();
	}

	public Identifier getEntityRegistryValue(EntityType type){
		return type.getRegistryEntry().registryKey().getValue();
	}
	public Identifier getItemRegistryValue(Item item){
		return item.getRegistryEntry().registryKey().getValue();
	}

	public Map<Identifier,Identifier> otherEntites(){
		Map<Identifier, Identifier> map = new HashMap<>();
		EntityType<?>[] entityTypes = {
				EntityType.BOAT,
				EntityType.CHEST_BOAT,
				EntityType.AREA_EFFECT_CLOUD,
				EntityType.DRAGON_FIREBALL,
				EntityType.ENDER_DRAGON,
				EntityType.EVOKER_FANGS,
				EntityType.EXPERIENCE_ORB,
				EntityType.EYE_OF_ENDER,
				EntityType.FALLING_BLOCK,
				EntityType.GIANT,
				EntityType.IRON_GOLEM,
				EntityType.ITEM,
				EntityType.FIREBALL,
				EntityType.LEASH_KNOT,
				EntityType.LIGHTNING_BOLT,
				EntityType.LLAMA_SPIT,
				EntityType.MARKER,
				EntityType.SPAWNER_MINECART,
				EntityType.SHULKER_BULLET,
				EntityType.SMALL_FIREBALL,
				EntityType.SNOW_GOLEM,
				EntityType.WITHER,
				EntityType.WITHER_SKULL,
				EntityType.FISHING_BOBBER,
				EntityType.PLAYER
		};
		Item[] items = {
				Items.OAK_BOAT,
				Items.OAK_CHEST_BOAT,
				Items.LINGERING_POTION,
				Items.DRAGON_BREATH,
				Items.DRAGON_HEAD,
				Items.SHEARS,
				Items.EXPERIENCE_BOTTLE,
				Items.ENDER_EYE,
				Items.SAND,
				Items.ZOMBIE_SPAWN_EGG,
				Items.IRON_BLOCK,
				Items.REDSTONE,
				Items.FIRE_CHARGE,
				Items.LEAD,
				Items.LIGHTNING_ROD,
				Items.GHAST_TEAR,
				Items.BARRIER,
				Items.SPAWNER,
				Items.GHAST_TEAR,
				Items.FIRE_CHARGE,
				Items.JACK_O_LANTERN,
				Items.NETHER_STAR,
				Items.WITHER_SKELETON_SKULL,
				Items.FISHING_ROD,
				Items.PLAYER_HEAD
		};
		for (int i = 0; i < entityTypes.length; i++) {
			map.put(getEntityRegistryValue(entityTypes[i]), getItemRegistryValue(items[i]));
		}
//		map.put(getEntityRegistryValue(EntityType.BOAT),getItemRegistryValue(Items.OAK_BOAT));
//		map.put(getEntityRegistryValue(EntityType.CHEST_BOAT),getItemRegistryValue(Items.OAK_CHEST_BOAT));
//		map.put(getEntityRegistryValue(EntityType.AREA_EFFECT_CLOUD),getItemRegistryValue(Items.LINGERING_POTION));
//		map.put(getEntityRegistryValue(EntityType.DRAGON_FIREBALL),getItemRegistryValue(Items.DRAGON_BREATH));
//		map.put(getEntityRegistryValue(EntityType.ENDER_DRAGON),getItemRegistryValue(Items.DRAGON_HEAD));
//		map.put(getEntityRegistryValue(EntityType.EVOKER_FANGS),getItemRegistryValue(Items.SHEARS));
//		map.put(getEntityRegistryValue(EntityType.EXPERIENCE_ORB),getItemRegistryValue(Items.EXPERIENCE_BOTTLE));
//		map.put(getEntityRegistryValue(EntityType.EYE_OF_ENDER),getItemRegistryValue(Items.ENDER_EYE));
//		map.put(getEntityRegistryValue(EntityType.FALLING_BLOCK),getItemRegistryValue(Items.SAND));
//		map.put(getEntityRegistryValue(EntityType.GIANT),getItemRegistryValue(Items.ZOMBIE_SPAWN_EGG));
//		map.put(getEntityRegistryValue(EntityType.IRON_GOLEM),getItemRegistryValue(Items.IRON_BLOCK));
//		map.put(getEntityRegistryValue(EntityType.ITEM),getItemRegistryValue(Items.REDSTONE));
//		map.put(getEntityRegistryValue(EntityType.FIREBALL),getItemRegistryValue(Items.FIRE_CHARGE));
//		map.put(getEntityRegistryValue(EntityType.LEASH_KNOT),getItemRegistryValue(Items.LEAD));
//		map.put(getEntityRegistryValue(EntityType.LIGHTNING_BOLT),getItemRegistryValue(Items.LIGHTNING_ROD));
//		map.put(getEntityRegistryValue(EntityType.LLAMA_SPIT),getItemRegistryValue(Items.GHAST_TEAR));
//		map.put(getEntityRegistryValue(EntityType.MARKER),getItemRegistryValue(Items.BARRIER));
//		map.put(getEntityRegistryValue(EntityType.SPAWNER_MINECART),getItemRegistryValue(Items.SPAWNER));
//		map.put(getEntityRegistryValue(EntityType.SHULKER_BULLET),getItemRegistryValue(Items.GHAST_TEAR));
//		map.put(getEntityRegistryValue(EntityType.SMALL_FIREBALL),getItemRegistryValue(Items.FIRE_CHARGE));
//		map.put(getEntityRegistryValue(EntityType.SNOW_GOLEM),getItemRegistryValue(Items.JACK_O_LANTERN));
//		map.put(getEntityRegistryValue(EntityType.WITHER),getItemRegistryValue(Items.NETHER_STAR));
//		map.put(getEntityRegistryValue(EntityType.WITHER_SKULL),getItemRegistryValue(Items.WITHER_SKELETON_SKULL));
//		map.put(getEntityRegistryValue(EntityType.FISHING_BOBBER),getItemRegistryValue(Items.FISHING_ROD));
//		map.put(getEntityRegistryValue(EntityType.PLAYER),getItemRegistryValue(Items.PLAYER_HEAD));
		return map;
	}
}
