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
import org.hydrogenhack.gui.EntityMenuEditScreen;
import org.hydrogenhack.module.ModuleManager;
import org.hydrogenhack.module.mods.EntityMenu;
import org.hydrogenhack.util.BleachQueue;
import org.hydrogenhack.util.collections.MutablePairList;

/**
 * @author <a href="https://github.com/lasnikprogram">Lasnik</a>
 */
public class CmdEntityMenu extends Command {

	public CmdEntityMenu() {
		super("entitymenu", "Opens the gui to manage the things which appear on the entitymenu interaction screen.", "entitymenu", CommandCategory.MODULES,
				"playermenu", "interactionmenu");
	}

	@Override
	public void onCommand(String alias, String[] args) throws Exception {
		MutablePairList<String, String> interactions = ModuleManager.getModule(EntityMenu.class).interactions;

		BleachQueue.add(() -> mc.setScreen(new EntityMenuEditScreen(interactions)));
	}
}
