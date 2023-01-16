/*
 * This file is part of the BleachHack distribution (https://github.com/BleachDev/BleachHack/).
 * Copyright (c) 2021 Bleach and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package org.hydrogenhack.command.commands;

import org.hydrogenhack.HydrogenHack;
import org.hydrogenhack.command.Command;
import org.hydrogenhack.command.CommandCategory;
import org.hydrogenhack.command.exception.CmdSyntaxException;
import org.hydrogenhack.util.BleachLogger;
import org.hydrogenhack.util.io.BleachFileHelper;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.minecraft.text.Text;



public class CmdWatermark extends Command {

	public CmdWatermark() {
		super("watermark", "Sets the client watermark.", "watermark reset [color/text] | watermark text <text> | watermark color <color 1> <color 2>", CommandCategory.MISC);

		JsonElement text1 = BleachFileHelper.readMiscSetting("watermarkText1");
		JsonElement text2 = BleachFileHelper.readMiscSetting("watermarkText2");
		JsonElement color1 = BleachFileHelper.readMiscSetting("watermarkColor1");
		JsonElement color2 = BleachFileHelper.readMiscSetting("watermarkColor2");

		if (text1 != null && text1.isJsonPrimitive() && text2 != null && text2.isJsonPrimitive()) {
			HydrogenHack.watermark.setStrings(text1.getAsString(), text2.getAsString());
		}

		if (color1 != null && color1.isJsonPrimitive() && color1.getAsJsonPrimitive().isNumber()
				&& color2 != null && color2.isJsonPrimitive() && color2.getAsJsonPrimitive().isNumber()) {
			HydrogenHack.watermark.setColor(color1.getAsInt(), color2.getAsInt());
		}
	}

	@Override
	public void onCommand(String alias, String[] args) throws Exception {
		if (args.length == 0) {
			throw new CmdSyntaxException();
		}

		if (args[0].equalsIgnoreCase("reset")) {
			if (args.length == 1) {
				HydrogenHack.watermark.reset(true, true);
				saveText();
				saveColor();

				BleachLogger.info("Reset the watermark text and colors!");
			} else if (args[1].equalsIgnoreCase("color")) {
				HydrogenHack.watermark.reset(false, true);
				saveColor();

				BleachLogger.info("Reset the watermark colors!");
			} else if (args[1].equalsIgnoreCase("text")) {
				HydrogenHack.watermark.reset(true, false);
				saveText();

				BleachLogger.info("Reset the watermark text!");
			} else {
				throw new CmdSyntaxException();
			}
		} else {
			if (args.length == 1) {
				throw new CmdSyntaxException();
			}

			if (args[0].equalsIgnoreCase("text")) {
				if (args.length > 3) {
					throw new CmdSyntaxException("The watermark can't contain more than 2 words.");
				}

				if ((args.length == 2 && args[1].length() < 2) || (args.length == 3 && (args[1].isEmpty() || args[2].isEmpty()))) {
					throw new CmdSyntaxException("The watermark can't be less than 2 characters long.");
				}

				HydrogenHack.watermark.setStrings(args[1], args.length == 3 ? args[2] : "");
				saveText();

				BleachLogger.info(Text.literal("Set the watermark to ").append(HydrogenHack.watermark.getText()));
			} else if (args[0].equalsIgnoreCase("color")) {
				if (args.length > 3) {
					throw new CmdSyntaxException("The watermark can't contain more than 2 colors.");
				}

				HydrogenHack.watermark.setColor(
						Integer.parseInt(args[1].replace("x", "").replace("#", ""), 16),
						args.length == 3 ? Integer.parseInt(args[2].replace("x", "").replace("#", ""), 16) : HydrogenHack.watermark.getColor2());
				saveColor();
				
				BleachLogger.info(Text.literal("Set the watermark to ").append(HydrogenHack.watermark.getText()));
			} else {
				throw new CmdSyntaxException();
			}
		}
	}

	private void saveColor() {
		BleachFileHelper.saveMiscSetting("watermarkColor1", new JsonPrimitive(HydrogenHack.watermark.getColor1()));
		BleachFileHelper.saveMiscSetting("watermarkColor2", new JsonPrimitive(HydrogenHack.watermark.getColor2()));
	}
	
	private void saveText() {
		BleachFileHelper.saveMiscSetting("watermarkText1", new JsonPrimitive(HydrogenHack.watermark.getString1()));
		BleachFileHelper.saveMiscSetting("watermarkText2", new JsonPrimitive(HydrogenHack.watermark.getString2()));
	}
}
