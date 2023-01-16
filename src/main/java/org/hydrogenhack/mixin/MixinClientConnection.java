/*
 * This file is part of the BleachHack distribution (https://github.com/BleachDev/BleachHack/).
 * Copyright (c) 2021 Bleach and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package org.hydrogenhack.mixin;

import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import org.hydrogenhack.HydrogenHack;
import org.hydrogenhack.command.Command;
import org.hydrogenhack.command.CommandManager;
import org.hydrogenhack.event.events.EventPacket;
import org.hydrogenhack.module.ModuleManager;
import org.hydrogenhack.module.mods.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;

import static net.minecraft.util.math.MathHelper.sign;

@Mixin(ClientConnection.class)
public class MixinClientConnection {

	@Shadow private Channel channel;

	@Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
	private void channelRead0(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo callback) {
		if (this.channel.isOpen() && packet != null) {
			EventPacket.Read event = new EventPacket.Read(packet);
			HydrogenHack.eventBus.post(event);

			if (event.isCancelled()) {
				callback.cancel();
			} else if (packet instanceof PlayerListS2CPacket) {
				handlePlayerList((PlayerListS2CPacket) packet);
			}
		}
	}

	@Inject(method = "send(Lnet/minecraft/network/Packet;Lnet/minecraft/network/PacketCallbacks;)V", at = @At("HEAD"), cancellable = true)
	private void send(Packet<?> packet, PacketCallbacks packetCallback, CallbackInfo callback) {
		if (packet instanceof ChatMessageC2SPacket) {
			if (!CommandManager.allowNextMsg) {
				ChatMessageC2SPacket pack = (ChatMessageC2SPacket) packet;
				if (pack.chatMessage().startsWith(Command.getPrefix())) {
					CommandManager.callCommand(pack.chatMessage().substring(Command.getPrefix().length()));
					callback.cancel();
				}
			}

			CommandManager.allowNextMsg = false;
		}
		if (packet instanceof VehicleMoveC2SPacket){
			IamBot mod = ModuleManager.getModule(IamBot.class);
			if (mod.isEnabled()) {
				VehicleMoveC2SPacket pack = (VehicleMoveC2SPacket) packet;
				((AccessorVehicleMove) pack).setX(round_fix(pack.getX()));
				((AccessorVehicleMove) pack).setZ(round_fix(pack.getZ()));
			}
		}

		EventPacket.Send event = new EventPacket.Send(packet);
		HydrogenHack.eventBus.post(event);

		if (event.isCancelled()) {
			callback.cancel();
		}
	}
	@Inject(method="handlePacket",at = @At("HEAD"),cancellable = true)
	private static <T extends PacketListener> void injectHandlePacket(Packet<T> packet, PacketListener listener, CallbackInfo ci) {
		// Cancel Worldborder
		NoBorder modBorder = ModuleManager.getModule(NoBorder.class);
		if (packet.getClass() == net.minecraft.network.packet.s2c.play.WorldBorderInitializeS2CPacket.class || packet.getClass() == net.minecraft.network.packet.s2c.play.WorldBorderCenterChangedS2CPacket.class || packet.getClass() == net.minecraft.network.packet.s2c.play.WorldBorderInterpolateSizeS2CPacket.class || packet.getClass() == net.minecraft.network.packet.s2c.play.WorldBorderSizeChangedS2CPacket.class) {
			if (modBorder.isEnabled()) {
				ci.cancel();
			}

		}
		//Cancel Demo Screen
		FuckThosePopups modPopups = ModuleManager.getModule(FuckThosePopups.class);
		if (packet.getClass() == GameStateChangeS2CPacket.class) {
			if (modPopups.isEnabled()) {
				if ((((GameStateChangeS2CPacket) packet).getReason() == new GameStateChangeS2CPacket.Reason(5))) {
					ci.cancel();
				}
				if ((((GameStateChangeS2CPacket) packet).getReason() == new GameStateChangeS2CPacket.Reason(4))) {
					ci.cancel();
				}
				if ((((GameStateChangeS2CPacket) packet).getReason() == new GameStateChangeS2CPacket.Reason(3))) {
					ci.cancel();
				}

			}
		}
	}

	private static double round_fix(double value){
		double temp = (double) (Math.round(value*100))/100;
		return (double) Math.nextAfter(temp, temp + sign(value));
	}
	private void handlePlayerList(PlayerListS2CPacket packet) {
		if (packet.getAction() == PlayerListS2CPacket.Action.ADD_PLAYER) {
			HydrogenHack.playerMang.addQueueEntries(packet.getEntries());
		} else if (packet.getAction() == PlayerListS2CPacket.Action.REMOVE_PLAYER) {
			HydrogenHack.playerMang.removeQueueEntries(packet.getEntries());
		}
	}
}