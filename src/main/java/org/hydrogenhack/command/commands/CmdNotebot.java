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
import org.hydrogenhack.gui.NotebotScreen;
import org.hydrogenhack.util.BleachQueue;

public class CmdNotebot extends Command {

	public CmdNotebot() {
		super("notebot", "Shows the notebot gui.", "notebot", CommandCategory.MODULES);
	}

	@Override
	public void onCommand(String alias, String[] args) throws Exception {
		BleachQueue.add(() -> mc.setScreen(new NotebotScreen()));
	}

}
