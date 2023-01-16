/*
 * This file is part of the BleachHack distribution (https://github.com/BleachDev/BleachHack/).
 * Copyright (c) 2021 Bleach and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package org.hydrogenhack.module.mods;

import org.hydrogenhack.event.events.EventTick;
import org.hydrogenhack.eventbus.BleachSubscribe;
import org.hydrogenhack.module.Module;
import org.hydrogenhack.module.ModuleCategory;

public class AutoWalk extends Module {

	public AutoWalk() {
		super("AutoWalk", KEY_UNBOUND, ModuleCategory.MOVEMENT, "Automatically holds your walk key down.");
	}

	@Override
	public void onDisable(boolean inWorld) {
		mc.options.forwardKey.setPressed(false);

		super.onDisable(inWorld);
	}

	@BleachSubscribe
	public void onTick(EventTick event) {
		mc.options.forwardKey.setPressed(true);
	}
}
