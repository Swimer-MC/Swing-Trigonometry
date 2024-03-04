package org.restudios.renderers;

import org.ReStudios.utitlitium.ArrayUtils;
import org.ReStudios.utitlitium.MathUtils;
import org.ReStudios.utitlitium.vectors.Vector2;
import org.ReStudios.utitlitium.vectors.Vector2d;
import org.jetbrains.annotations.Nullable;
import org.restudios.IRenderer;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class SmoothMouse implements IRenderer {
    ArrayList<Vector2> history = new ArrayList<>();
    long i = 0;
    boolean enabled = false;
    @Override
    public void render(Graphics2D g, @Nullable Vector2 mouse, Vector2 size) {
        if(enabled){
            g.setColor(Color.GREEN);
            g.fillRect(size.x/2-50, size.y/5-35, 100, 60);
            g.setColor(Color.WHITE);
            int s = 20;
            while (width("ON", g) < 100){
                s++;
                g.setFont(g.getFont().deriveFont((float)s));
            }
            g.drawString("ON", size.x/2-50, size.y/5+20);
        }else{
            g.setColor(Color.RED);
            g.fillRect(size.x/2-70, size.y/5-35, 140, 60);
            g.setColor(Color.WHITE);
            int s = 20;
            while (width("OFF", g) < 140){
                s++;
                g.setFont(g.getFont().deriveFont((float)s));
            }
            g.drawString("OFF", size.x/2-70, size.y/5+20);
        }

        if(System.currentTimeMillis() - i >= 1){
            ArrayUtils.addLimited(history, 10, mouse);
            i = System.currentTimeMillis();
        }
        Vector2d v = MathUtils.getBezierPoint(history.stream().map(vector2 -> new Vector2d(vector2.x, vector2.y)).toList().toArray(new Vector2d[0]), 0.5);

        Vector2 vec = enabled ? v.asVector2() : mouse;
        if(vec==null) return;
        vec.clone().sub(3);
        g.setColor(Color.BLACK);
        g.fillOval(vec.x, vec.y, 6, 6);
    }

    @Override
    public void keyRelease(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            enabled = !enabled;
        }
    }

    public static int width(String text, Graphics2D g2d){
        return (int) g2d.getFont().getStringBounds(text, g2d.getFontRenderContext()).getWidth();
    }
}
