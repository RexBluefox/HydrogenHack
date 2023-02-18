package org.hydrogenhack.util;

import com.mojang.blaze3d.systems.RenderSystem;
import org.joml.Matrix4f;

import static org.hydrogenhack.HydrogenHack.mc;


public class Utils {
    public static boolean rendering3D = true;
//    public static void unscaledProjection() {
//        RenderSystem.setProjectionMatrix(new Matrix4f().setOrtho(0, mc.getWindow().getFramebufferWidth(), mc.getWindow().getFramebufferHeight(), 0, 1000, 3000));
//        rendering3D = false;
//    }
//    public static void scaledProjection() {
//        RenderSystem.setProjectionMatrix(new Matrix4f().setOrtho(0, (float) (mc.getWindow().getFramebufferWidth() / mc.getWindow().getScaleFactor()), (float) (mc.getWindow().getFramebufferHeight() / mc.getWindow().getScaleFactor()), 0, 1000, 3000));
//        rendering3D = true;
//    }
}
