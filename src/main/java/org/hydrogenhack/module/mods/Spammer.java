/*
 * This file is part of the BleachHack distribution (https://github.com/BleachDev/BleachHack/).
 * Copyright (c) 2021 Bleach and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package org.hydrogenhack.module.mods;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.hydrogenhack.command.Command;
import org.hydrogenhack.event.events.EventTick;
import org.hydrogenhack.eventbus.BleachSubscribe;
import org.hydrogenhack.module.Module;
import org.hydrogenhack.module.ModuleCategory;
import org.hydrogenhack.setting.module.SettingMode;
import org.hydrogenhack.setting.module.SettingSlider;
import org.hydrogenhack.util.io.BleachFileMang;

public class Spammer extends Module {

	private Random rand = new Random();
	private List<String> lines = new ArrayList<>();
	private int lineCount;
	private int tickCount;

	public Spammer() {
		super("Spammer", KEY_UNBOUND, ModuleCategory.MISC, "Spams chat with messages you set (edit with " + Command.getPrefix() + "spammer).",
				new SettingMode("Read", "Random", "Order").withDesc("How to read the spammer file."),
				new SettingSlider("Delay", 1, 120, 20, 0).withDesc("Delay between messages (in seconds)."));
	}

	@Override
	public void onEnable(boolean inWorld) {
		super.onEnable(inWorld);

		BleachFileMang.createFile("spammer.txt");
		lines = BleachFileMang.readFileLines("spammer.txt");
		lineCount = 0;
	}

	@BleachSubscribe
	public void onTick(EventTick event) {
		tickCount++;

		if (lines.isEmpty())
			return;

		if (tickCount % (getSetting(1).asSlider().getValueInt() * 20) == 0) {
			if (getSetting(0).asMode().getMode() == 0) {
				mc.player.sendChatMessage(lines.get(rand.nextInt(lines.size())), null);
			} else if (getSetting(0).asMode().getMode() == 1) {
				mc.player.sendChatMessage(lines.get(lineCount), null);
			}

			if (lineCount >= lines.size() - 1) {
				lineCount = 0;
			} else {
				lineCount++;
			}
		}
	}

}
