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
import org.hydrogenhack.gui.clickgui.ModuleClickGuiScreen;
import org.hydrogenhack.module.Module;
import org.hydrogenhack.module.ModuleCategory;
import org.hydrogenhack.setting.module.SettingSlider;
import org.hydrogenhack.setting.module.SettingToggle;
import org.lwjgl.glfw.GLFW;

public class ClickGui extends Module {

	public ClickGui() {
		super("ClickGui", GLFW.GLFW_KEY_RIGHT_SHIFT, ModuleCategory.RENDER, "Draws the clickgui.",
				new SettingSlider("Length", 70, 85, 75, 0).withDesc("The length of each window."),
				new SettingToggle("Search bar", true).withDesc("Shows a search bar."),
				new SettingToggle("Help", true).withDesc("Shows the help text."));
	}

	@Override
	public void onEnable(boolean inWorld) {
		super.onEnable(inWorld);

		mc.setScreen(ModuleClickGuiScreen.INSTANCE);
	}

	@Override
	public void onDisable(boolean inWorld) {
		if (mc.currentScreen instanceof ModuleClickGuiScreen)
			mc.setScreen(null);

		super.onDisable(inWorld);
	}

	@BleachSubscribe
	public void onOpenScreen(EventOpenScreen event) {
		if (event.getScreen() == null) {
			setEnabled(false);
		}
	}
}
