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
import org.hydrogenhack.util.BleachLogger;
import org.hydrogenhack.util.io.BleachFileMang;

import net.minecraft.util.Util;

public class CmdSpammer extends Command {

	public CmdSpammer() {
		super("spammer", "Opens the spammer file.", "spammer", CommandCategory.MODULES,
				"editspammer");
	}

	@Override
	public void onCommand(String alias, String[] args) throws Exception {
		BleachFileMang.createFile("spammer.txt");
		Util.getOperatingSystem().open(BleachFileMang.getDir().resolve("spammer.txt").toUri());

		BleachLogger.info("Opened spammer file.");
	}

}
