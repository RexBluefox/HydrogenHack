/*
 * This file is part of the BleachHack distribution (https://github.com/BleachDev/BleachHack/).
 * Copyright (c) 2021 Bleach and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package org.hydrogenhack.module.mods;

import org.hydrogenhack.event.events.EventOpenScreen;
import org.hydrogenhack.eventbus.BleachSubscribe;
import org.hydrogenhack.module.Module;
import org.hydrogenhack.module.ModuleCategory;
import org.hydrogenhack.setting.module.SettingSlider;
import org.hydrogenhack.setting.module.SettingToggle;
import org.hydrogenhack.util.BleachQueue;

import net.minecraft.client.gui.screen.DeathScreen;

public class AutoRespawn extends Module {

	public AutoRespawn() {
		super("AutoRespawn", KEY_UNBOUND, ModuleCategory.PLAYER, "Automatically respawn when you die.",
				new SettingToggle("Delay", false).withDesc("Adds a delay before respawing.").withChildren(
						new SettingSlider("Delay", 1, 15, 5, 0).withDesc("How many ticks to delay.")));
	}

	@BleachSubscribe
	public void onOpenScreen(EventOpenScreen event) {
		if (event.getScreen() instanceof DeathScreen) {
			if (getSetting(0).asToggle().getState()) {
				for (int i = 0; i <= getSetting(0).asToggle().getChild(0).asSlider().getValueInt(); i++) {
					BleachQueue.add("autorespawn", () -> {});
				}

				BleachQueue.add("autorespawn", () -> mc.player.requestRespawn());
			} else {
				mc.player.requestRespawn();
			}
		}
	}
}
