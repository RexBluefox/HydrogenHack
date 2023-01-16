package org.hydrogenhack.module.mods;

import org.hydrogenhack.module.Module;
import org.hydrogenhack.module.ModuleCategory;
import org.hydrogenhack.setting.module.SettingMode;
import org.hydrogenhack.setting.module.SettingSlider;

import static org.hydrogenhack.module.Module.KEY_UNBOUND;

public class TextureRotation extends Module {
    public TextureRotation() {
        super("Texture Rotation", KEY_UNBOUND, ModuleCategory.BLUEFOX, "Changes the Texture Rotation",
                new SettingMode("Mode","Random","Value").withDesc("Rotation Mode"),
                new SettingSlider("Rotation Value",0,3129871,1,0)
        );
    }
    public void onEnable(boolean inWorld) {
        mc.worldRenderer.reload();
    }


}
