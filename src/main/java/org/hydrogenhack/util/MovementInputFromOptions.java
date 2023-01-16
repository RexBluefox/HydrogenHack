package org.hydrogenhack.util;

import net.minecraft.client.RunArgs;
import net.minecraft.client.option.GameOptions;

public class MovementInputFromOptions extends MovementInput {
    private final GameOptions gameSettings;

    public MovementInputFromOptions(GameOptions p_i1237_1_) {
        this.gameSettings = p_i1237_1_;
    }

    public void updatePlayerMoveState() {
        this.moveStrafe = 0.0F;
        this.moveForward = 0.0F;
        if (this.gameSettings.forwardKey.isPressed()) {
            ++this.moveForward;
            this.forwardKeyDown = true;
        } else {
            this.forwardKeyDown = false;
        }

        if (this.gameSettings.backKey.isPressed()) {
            --this.moveForward;
            this.backKeyDown = true;
        } else {
            this.backKeyDown = false;
        }

        if (this.gameSettings.leftKey.isPressed()) {
            ++this.moveStrafe;
            this.leftKeyDown = true;
        } else {
            this.leftKeyDown = false;
        }

        if (this.gameSettings.rightKey.isPressed()) {
            --this.moveStrafe;
            this.rightKeyDown = true;
        } else {
            this.rightKeyDown = false;
        }

        this.jump = this.gameSettings.jumpKey.isPressed();
        this.sneak = this.gameSettings.sneakKey.isPressed();
        if (this.sneak) {
            this.moveStrafe = (float)((double)this.moveStrafe * 0.3);
            this.moveForward = (float)((double)this.moveForward * 0.3);
        }

    }
}
