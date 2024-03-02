package org.restudios.renderers;

import com.sun.source.tree.Tree;
import org.ReStudios.utitlitium.ArrayUtils;
import org.ReStudios.utitlitium.vectors.Vector2;
import org.jetbrains.annotations.Nullable;
import org.restudios.IRenderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class MouseFollow implements IRenderer {
    TreeMap<Long, Vector2> points = new TreeMap<>();

    @Override
    public void render(Graphics2D g, @Nullable Vector2 mouse, Vector2 size) {
        int grid = 5;
        int everyX = size.x() / grid;
        int everyY = everyX;
        int x = mouse.x() / everyX;
        int y = mouse.y() / everyY;
        Vector2 pos = new Vector2(x*everyX+everyX/2, y*everyY+everyY/2);
        g.setColor(Color.RED);
        Vector2 v = points.values().stream().findFirst().orElse(null);
        if(v == null || !v.equals(pos)){
            points.put(System.currentTimeMillis(), pos);
        }
        Vector2 before = v == null ? pos : v;
        g.setColor(Color.BLACK);
        for (Vector2 value : points.values()) {
            g.drawLine(before.x(), before.y(), value.x(), value.y());
            before = value;
        }
        clear();
    }
    private void clear(){
        ArrayList<Long> queue = new ArrayList<>();
        for (Long l : points.keySet()) {
            long s = l;
            if(System.currentTimeMillis() - s >= 1000_000){
                queue.add(l);
            }
        }
        for (Long l : queue) {
            points.remove(l);
        }
    }
}
