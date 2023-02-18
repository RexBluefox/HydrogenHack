package org.hydrogenhack.gui;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import org.hydrogenhack.gui.window.Window;
import org.hydrogenhack.gui.window.WindowScreen;
import org.hydrogenhack.gui.window.widget.WindowButtonWidget;

public class ChatFilterScreen extends WindowScreen {
    private int page = 0;
    public ChatFilterScreen() {
        super(Text.literal("Chat Filter Gui"));
    }


    public void init() {
        super.init();
        int ww = Math.max(width / 2, 360);
        int wh = Math.max(height / 2, 200);
        addWindow(new Window(
                width / 2 - ww / 2,
                height / 2 - wh / 2,
                width / 2 + ww / 2,
                height / 2 + wh / 2,
                "Chat Filter Gui", new ItemStack(Items.NOTE_BLOCK)));

//        getWindow(0).addWidget(new WindowButtonWidget(22, 14, 32, 24, "<", () -> page = page <= 0 ? 0 : page - 1));
//        getWindow(0).addWidget(new WindowButtonWidget(77, 14, 87, 24, ">", () -> page++));

        int xEnd = getWindow(0).x2 - getWindow(0).x1;
    }
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
    }
    public void onRenderWindow(MatrixStack matrices, int window, int mouseX, int mouseY) {
        super.onRenderWindow(matrices, window, mouseX, mouseY);

        if (window == 0) {
            int x = getWindow(0).x1;
            int y = getWindow(0).y1 + 10;
            int w = getWindow(0).x2 - x;
            int h = getWindow(0).y2 - y;
        }
    }
}
