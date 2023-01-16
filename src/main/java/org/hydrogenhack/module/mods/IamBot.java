package org.hydrogenhack.module.mods;

import org.hydrogenhack.module.Module;
import org.hydrogenhack.module.ModuleCategory;
import org.hydrogenhack.setting.module.SettingMode;

public class IamBot extends Module {

    public IamBot() {
        super("I am Bot", KEY_UNBOUND, ModuleCategory.PLAYER, "Rounds your movement packets");
    }
}
