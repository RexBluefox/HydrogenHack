package org.hydrogenhack.util;

import net.minecraft.entity.player.PlayerEntity;

import static org.hydrogenhack.HydrogenHack.mc;

public class MovementUtil {
    public static boolean isInputting() {
        PlayerEntity player = mc.player;
        if (player != null) {
            MovementInput movementInput = new MovementInputFromOptions(mc.options);
            return movementInput.moveForward != 0.0f || movementInput.moveStrafe != 0.0f;
        } else {
            return false;
        }
    }
    private static float roundedForward(){
        MovementInput movementInput = new MovementInputFromOptions(mc.options);
        return Math.signum(movementInput.moveForward);
    }
    private static float roundedStrafing(){
        MovementInput movementInput = new MovementInputFromOptions(mc.options);
        return Math.signum(movementInput.moveStrafe);
    }
    public static double calcMoveYaw(){
        float yawIn = mc.player.getYaw();
        float moveForward = roundedForward();
        float moveString = roundedStrafing();
        float strafe = 90 * moveString;
        strafe *= (moveForward != 0F) ? (moveForward * 0.5F) : 1F;

        float yaw = yawIn - strafe;
        yaw -= (moveForward < 0F) ? 180 : 0;

        return Math.toRadians(yaw);

    }
}
