package org.hydrogenhack.command.commands;

import net.minecraft.util.math.Vec3d;
import org.hydrogenhack.command.Command;
import org.hydrogenhack.command.CommandCategory;

public class CmdAlign extends Command {
    public CmdAlign() {
        super("align", "align yourself to coords", "align <x> <y> <z>", CommandCategory.MISC);
    }

    @Override
    public void onCommand(String alias, String[] args) throws Exception {
        int x = Integer.parseInt(args[0]);
        int y = Integer.parseInt(args[1]);
        int z = Integer.parseInt(args[2]);
        Vec3d at = new Vec3d(x, y, z);
        mc.player.lookAt(mc.player.getCommandSource().getEntityAnchor(),at);
    }
}
