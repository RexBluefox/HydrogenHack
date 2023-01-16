package org.hydrogenhack.module.mods;

import org.hydrogenhack.module.Module;
import org.hydrogenhack.module.ModuleCategory;
import org.hydrogenhack.setting.module.SettingColor;
import org.hydrogenhack.setting.module.SettingMode;
import org.hydrogenhack.setting.module.SettingSlider;
import org.hydrogenhack.setting.module.SettingToggle;

public class Testing extends Module {
    public Testing() {
        super("Testing", KEY_UNBOUND, ModuleCategory.BLUEFOX, "Just for testing");
    }
}
