package org.hydrogenhack.command.commands;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.hydrogenhack.command.Command;
import org.hydrogenhack.command.CommandCategory;
import org.hydrogenhack.command.CommandManager;
import org.hydrogenhack.event.events.EventTick;
import org.hydrogenhack.eventbus.BleachSubscribe;
import org.hydrogenhack.module.Module;
import org.hydrogenhack.module.ModuleManager;
import org.hydrogenhack.module.mods.PlayerViewer;
import org.hydrogenhack.util.BleachLogger;
import org.hydrogenhack.util.FakePlayerEntity;

import java.util.Objects;

public class CmdView extends Command {
    public CmdView() {
        super("view", "spectate a player", "view <player>", CommandCategory.MISC, "viewer");
    }
    public boolean toggled = false;
    public PlayerEntity viewingPlayer;
    private FakePlayerEntity fakePlayer;
    @Override
    public void onCommand(String alias, String[] args) throws Exception {
        String playerName = args[0];
        BleachLogger.logger.info("Initial: Trying to view " + playerName);
        assert mc.world != null;
        if (playerName.equalsIgnoreCase("exit")){
            for (Entity entity : mc.world.getEntities()) {
                if (entity instanceof PlayerEntity) {
                    BleachLogger.logger.info("Valid Entity:" + entity.getDisplayName().getString() + " " + entity.getDisplayName().getString().equals(mc.player.getDisplayName().getString()));
                    if (entity.getDisplayName().getString().equals(mc.player.getDisplayName().getString())) {
                        BleachLogger.logger.info(entity.getDisplayName().getString() + " is valid");
                        viewingPlayer = (PlayerEntity) entity;
                        BleachLogger.logger.info("First: Trying to view " + viewingPlayer.getDisplayName().getString());
                        if (!viewingPlayer.isAlive()){
                            BleachLogger.error("It seems like you are dead 💀");
                            return;
                        }
                        if(fakePlayer != null)
                        {
                            fakePlayer.resetPlayerPosition();
                            fakePlayer.despawn();
                        }
                        mc.player.getInventory().clone(viewingPlayer.getInventory());
                        toggled = false;
                        BleachLogger.info("Viewing: "+viewingPlayer.getDisplayName().getString());
                    }
                }
            }
            //toggled = false;
        } else {
            for (Entity entity : mc.world.getEntities()) {
                if (entity instanceof PlayerEntity) {
                    BleachLogger.logger.info("Valid Entity:" + entity.getDisplayName().getString() + " " + entity.getDisplayName().getString().equals(playerName));
                    if (entity.getDisplayName().getString().equals(playerName)) {
                        BleachLogger.logger.info(entity.getDisplayName().getString() + " is valid");
                        viewingPlayer = (PlayerEntity) entity;
                        BleachLogger.logger.info("First: Trying to view " + viewingPlayer.getDisplayName().getString());
                        if (!viewingPlayer.isAlive()){
                            BleachLogger.error("This Player is dead 💀");
                            return;
                        }
                        fakePlayer = new FakePlayerEntity();
                        toggled = true;
                        BleachLogger.info("Viewing: "+viewingPlayer.getDisplayName().getString());
                    }
                }
            }
        }
    }

}
