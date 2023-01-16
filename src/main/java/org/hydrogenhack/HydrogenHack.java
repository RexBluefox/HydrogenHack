/*
 * This file is part of the BleachHack distribution (https://github.com/BleachDev/BleachHack/).
 * Copyright (c) 2021 Bleach and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package org.hydrogenhack;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.fabricmc.api.ModInitializer;
import net.minecraft.SharedConstants;

import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.CompletableFuture;

import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.Level;
import org.hydrogenhack.command.CommandManager;
import org.hydrogenhack.command.CommandSuggestor;
import org.hydrogenhack.eventbus.BleachEventBus;
import org.hydrogenhack.eventbus.handler.InexactEventHandler;
import org.hydrogenhack.gui.BleachTitleScreen;
import org.hydrogenhack.gui.clickgui.ModuleClickGuiScreen;
import org.hydrogenhack.module.ModuleManager;
import org.hydrogenhack.setting.option.Option;
import org.hydrogenhack.util.BleachLogger;
import org.hydrogenhack.util.BleachPlayerManager;
import org.hydrogenhack.util.FriendManager;
import org.hydrogenhack.util.Watermark;
import org.hydrogenhack.util.io.BleachFileHelper;
import org.hydrogenhack.util.io.BleachFileMang;
import org.hydrogenhack.util.io.BleachJsonHelper;
import org.hydrogenhack.util.io.BleachOnlineMang;

public class HydrogenHack implements ModInitializer {

	private static HydrogenHack instance = null;

	public static final String VERSION = "1.2.6";
	public static final int INTVERSION = 40;
	public static Watermark watermark;

	public static BleachEventBus eventBus;

	public static FriendManager friendMang;
	public static BleachPlayerManager playerMang;

	private static CompletableFuture<JsonObject> updateJson;

	//private BleachFileMang bleachFileManager;

	public static HydrogenHack getInstance() {
		return instance;
	}
	public static MinecraftClient mc;

	public HydrogenHack() {
		if (instance != null) {
			throw new RuntimeException("A BleachHack instance already exists.");
		}
	}

	// Phase 1
	// TODO: base-rewrite
	@Override
	public void onInitialize() {
		long initStartTime = System.currentTimeMillis();

		instance = this;
		watermark = new Watermark();
		eventBus = new BleachEventBus(new InexactEventHandler("bleachhack"), BleachLogger.logger);

		friendMang = new FriendManager();
		playerMang = new BleachPlayerManager();

		//this.eventBus = new EventBus();
		//this.bleachFileManager = new BleachFileMang();

		BleachFileMang.init();
		mc = MinecraftClient.getInstance();
		BleachFileHelper.readOptions();
		BleachFileHelper.readFriends();

		if (Option.PLAYERLIST_SHOW_AS_BH_USER.getValue()) {
			playerMang.startPinger();
		}

		if (Option.GENERAL_CHECK_FOR_UPDATES.getValue()) {
			updateJson = BleachOnlineMang.getResourceAsync("update/" + SharedConstants.getGameVersion().getName().replace(' ', '_') + ".json", BodyHandlers.ofString())
					.thenApply(s -> BleachJsonHelper.parseOrNull(s, JsonObject.class));
		}

		JsonElement mainMenu = BleachFileHelper.readMiscSetting("customTitleScreen");
		if (mainMenu != null && !mainMenu.getAsBoolean()) {
			BleachTitleScreen.customTitleScreen = false;
		}

		BleachLogger.logger.log(Level.INFO, "Loaded BleachHack (Phase 1) in %d ms.", System.currentTimeMillis() - initStartTime);
	}

	// Phase 2
	// Called after most of the game has been initialized in MixinMinecraftClient so all game resources can be accessed
	public void postInit() {
		long initStartTime = System.currentTimeMillis();

		ModuleManager.loadModules(this.getClass().getClassLoader().getResourceAsStream("hydrogenhack.modules.json"));
		BleachFileHelper.readModules();

		// TODO: move ClickGui and UI to phase 1
		ModuleClickGuiScreen.INSTANCE.initWindows();
		BleachFileHelper.readClickGui();
		BleachFileHelper.readUI();

		CommandManager.loadCommands(this.getClass().getClassLoader().getResourceAsStream("hydrogenhack.commands.json"));
		CommandSuggestor.start();

		BleachFileHelper.startSavingExecutor();

		BleachLogger.logger.log(Level.INFO, "Loaded BleachHack (Phase 2) in %d ms.", System.currentTimeMillis() - initStartTime);
	}

	public static JsonObject getUpdateJson() {
		try {
			return updateJson.get();
		} catch (Exception e) {
			return null;
		}
	}
}
