package org.restudios.renderers;

import org.ReStudios.utitlitium.vectors.Vector2;
import org.jetbrains.annotations.Nullable;
import org.restudios.IRenderer;

import java.awt.*;
import java.util.Random;

public class CPUTest implements IRenderer {
    @Override
    public void render(Graphics2D g2d, @Nullable Vector2 mouse, Vector2 size) {
        //int offset = 0;
        int w = size.x(), h = size.y();
        Random r= new Random();
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int rr = r.nextInt(255);
                int g = r.nextInt(255);
                int b = r.nextInt(255);
                g2d.setColor(new Color(rr, g, b));
                g2d.drawRect(x, y, 1, 1);
            }
        }
    }
}
