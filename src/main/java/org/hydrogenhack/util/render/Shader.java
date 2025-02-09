package org.hydrogenhack.util.render;

import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.util.Identifier;
import org.apache.commons.io.IOUtils;
import org.hydrogenhack.HydrogenHack;
import org.hydrogenhack.util.BleachLogger;
import org.hydrogenhack.util.render.color.QuadColor;
import net.minecraft.util.math.Matrix4f;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.hydrogenhack.HydrogenHack.mc;
import static org.lwjgl.opengl.GL32C.*;

public class Shader {
    public static Shader BOUND;

    private final int id;
    private final Object2IntMap<String> uniformLocations = new Object2IntOpenHashMap<>();

    public Shader(String vertPath, String fragPath) {
        int vert = GL.createShader(GL_VERTEX_SHADER);
        GL.shaderSource(vert, read(vertPath));

        String vertError = GL.compileShader(vert);
        if (vertError != null) {
            BleachLogger.logger.error("Failed to compile vertex shader (" + vertPath + "): " + vertError);
            throw new RuntimeException("Failed to compile vertex shader (" + vertPath + "): " + vertError);
        }

        int frag = GL.createShader(GL_FRAGMENT_SHADER);
        GL.shaderSource(frag, read(fragPath));

        String fragError = GL.compileShader(frag);
        if (fragError != null) {
            BleachLogger.logger.error("Failed to compile fragment shader (" + fragPath + "): " + fragError);
            throw new RuntimeException("Failed to compile fragment shader (" + fragPath + "): " + fragError);
        }

        id = GL.createProgram();

        String programError = GL.linkProgram(id, vert, frag);
        if (programError != null) {
            BleachLogger.logger.error("Failed to link program: " + programError);

            throw new RuntimeException("Failed to link program: " + programError);
        }

        GL.deleteShader(vert);
        GL.deleteShader(frag);
    }

    private String read(String path) {
        try {
            return IOUtils.toString(mc.getResourceManager().getResource(new Identifier("shaders/" + path)).get().getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void bind() {
        GL.useProgram(id);
        BOUND = this;
    }

    private int getLocation(String name) {
        if (uniformLocations.containsKey(name)) return uniformLocations.getInt(name);

        int location = GL.getUniformLocation(id, name);
        uniformLocations.put(name, location);
        return location;
    }

    public void set(String name, boolean v) {
        GL.uniformInt(getLocation(name), v ? GL_TRUE : GL_FALSE);
    }

    public void set(String name, int v) {
        GL.uniformInt(getLocation(name), v);
    }

    public void set(String name, double v) {
        GL.uniformFloat(getLocation(name), (float) v);
    }

    public void set(String name, double v1, double v2) {
        GL.uniformFloat2(getLocation(name), (float) v1, (float) v2);
    }

    public void set(String name, QuadColor color) {
        int r = color.getAllColors()[0];
        int g = color.getAllColors()[1];
        int b = color.getAllColors()[2];
        int a = color.getAllColors()[3];
        GL.uniformFloat4(getLocation(name), (float) r / 255, (float) g / 255, (float) b / 255, (float) a / 255);
    }

    public void set(String name, Matrix4f mat) {
        //GL.uniformMatrix(getLocation(name), mat);
    }

    public void setDefaults() {
        set("u_Proj", RenderSystem.getProjectionMatrix());
        set("u_ModelView", RenderSystem.getModelViewStack().peek().getPositionMatrix());
    }
}
