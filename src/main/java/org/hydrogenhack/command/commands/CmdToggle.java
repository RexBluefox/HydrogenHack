/*
 * This file is part of the BleachHack distribution (https://github.com/BleachDev/BleachHack/).
 * Copyright (c) 2021 Bleach and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package org.hydrogenhack.command.commands;

import org.hydrogenhack.command.Command;
import org.hydrogenhack.command.CommandCategory;
import org.hydrogenhack.command.exception.CmdSyntaxException;
import org.hydrogenhack.module.Module;
import org.hydrogenhack.module.ModuleManager;
import org.hydrogenhack.util.BleachLogger;
import org.hydrogenhack.util.BleachQueue;

public class CmdToggle extends Command {

	public CmdToggle() {
		super("toggle", "Toggles a mod with a command.", "toggle <module>", CommandCategory.MODULES);
	}

	@Override
	public void onCommand(String alias, String[] args) throws Exception {
		if (args.length != 1) {
			throw new CmdSyntaxException();
		}

		for (Module m : ModuleManager.getModules()) {
			if (args[0].equalsIgnoreCase(m.getName())) {
				BleachQueue.add(m::toggle);
				BleachLogger.info(m.getName() + " Toggled");
				return;
			}
		}

		BleachLogger.error("Module \"" + args[0] + "\" Not Found!");
	}

}
