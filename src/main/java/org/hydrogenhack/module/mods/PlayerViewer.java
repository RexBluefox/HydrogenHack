package org.hydrogenhack.module.mods;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import org.hydrogenhack.command.CommandManager;
import org.hydrogenhack.command.commands.CmdView;
import org.hydrogenhack.event.events.EventTick;
import org.hydrogenhack.eventbus.BleachSubscribe;
import org.hydrogenhack.mixin.AccessorCamera;
import org.hydrogenhack.module.Module;
import org.hydrogenhack.module.ModuleCategory;
import org.hydrogenhack.setting.module.ModuleSetting;
import org.hydrogenhack.util.BleachLogger;

public class PlayerViewer extends Module{
    public PlayerViewer() {
        super("Player Viewer" , KEY_UNBOUND, ModuleCategory.BLUEFOX, "See as another player");
    }

    @Override
    public void onEnable(boolean inWorld) {
        PlayerEntity viewingPlayer = CommandManager.getCommand(CmdView.class).viewingPlayer;
        mc.player.noClip = true;

        super.onEnable(inWorld);
    }

    @BleachSubscribe
    public void onTick(EventTick event){
        //BleachLogger.logger.info("Tick");
        if (CommandManager.getCommand(CmdView.class).toggled) {
            assert mc.player != null;
            if (mc.player.isAlive()) {
                //BleachLogger.logger.info("Tick inside");
                PlayerEntity viewingPlayer = CommandManager.getCommand(CmdView.class).viewingPlayer;
                //BleachLogger.logger.info("Trying to view " + viewingPlayer.getDisplayName().getString());
                ((AccessorCamera) mc.gameRenderer.getCamera()).setFocusedEntity(viewingPlayer);
                assert mc.player != null;
                mc.setCameraEntity(viewingPlayer);
                mc.player.getInventory().clone(viewingPlayer.getInventory());
                ItemStack itemStackMain = new ItemStack(viewingPlayer.getMainHandStack().getItem());
                ItemStack itemStackOff = new ItemStack((viewingPlayer.getOffHandStack().getItem()));
                mc.player.setStackInHand(Hand.MAIN_HAND, itemStackMain);
                mc.player.setStackInHand(Hand.OFF_HAND, itemStackOff);
                mc.player.setHealth(viewingPlayer.getHealth());
                mc.player.getHungerManager().setFoodLevel(viewingPlayer.getHungerManager().getFoodLevel());
                mc.getSkinProvider().loadSkin(viewingPlayer.getGameProfile());
            }
        }
    }
}
