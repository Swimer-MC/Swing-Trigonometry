package org.restudios;

import org.ReStudios.utitlitium.ArrayUtils;
import org.ReStudios.utitlitium.MathUtils;
import org.ReStudios.utitlitium.NFile;
import org.ReStudios.utitlitium.Timer;
import org.ReStudios.utitlitium.vectors.Vector2;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

public class Renderer {

    public static int fps = 0;
    private static long lastTime = System.currentTimeMillis();
    private static int frameCount = 0;
    public static Font font;
    public static Font f3;

    static {
        try {
            font = Font.createFonts(new NFile("Anta-Regular.ttf"))[0].deriveFont(Font.PLAIN, 20);
            f3 = Font.createFonts(new NFile("Ticketing.ttf"))[0].deriveFont(Font.PLAIN, 22);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    Vector2 before = new Vector2(0,0);
    TreeMap<Long, Double> angles = new TreeMap<>();
    public void render(Graphics2D g, @Nullable Vector2 mouse, Vector2 size){
        Timer tim = new Timer();
        tim.run();
        if(mouse == null) mouse = before;
        before = mouse;
        Vector2 s = mouse.clone().sub(3);
        int w = 100, h = 100;
        int cX = size.x()/2, cY = size.y()/2;
        double angle = 360 - new Vector2(cX, cY).getAngle(mouse);
        putCache(angle);
        g.setColor(Color.decode("#000000"));

        // рисуем центральный круг и горизотальную линию на 0 градусов
        g.drawOval(cX-w/2, cY-h/2, w, h);
        g.drawLine(cX, cY, cX+w/2, cY);


        // рисуем отображение угла от нуля градусов до линии курсора
        int radius = 30;
        int diameter = radius * 2;
        int arcX = cX - radius;
        int arcY = cY - radius;
        Arc2D arc = new Arc2D.Double(arcX, arcY, diameter, diameter, 0, angle, Arc2D.OPEN);
        g.setColor(Color.decode("#cccccc"));
        if(angle != 90 && angle != 270) {
            g.draw(arc);
        }else{
            if(angle == 90){
                g.drawLine(cX+radius, cY, cX+radius, cY-radius);
                g.drawLine(cX, cY-radius, cX+radius, cY-radius);
            }else{
                g.drawLine(cX+radius, cY, cX+radius, cY+radius);
                g.drawLine(cX, cY+radius, cX+radius, cY+radius);
            }
        }

        // Надпись градусов над курсором
        g.setFont(font);
        g.setColor(Color.BLACK);
        int sx = cX-w/2;
        int sY = cY+h/2+80;
        int spacing = (int)font.getStringBounds("|", g.getFontRenderContext()).getHeight();
        double a = Math.toRadians(angle);
        int fa = (int) angle;
        ArrayList<String> info = new ArrayList<>();
        info.add("sin = "+String.format("%.2f", Math.sin(a)));
        info.add("cos = "+String.format("%.2f", Math.cos(a)));
        info.add("tg = "+(fa == 90 || fa == 270 ? "infinity" : String.format("%.2f", Math.tan(a))));
        info.add("ctg = "+(fa == 180 ? "infinity" : String.format("%.2f", Math.toRadians(Math.cos(a))/Math.toRadians(Math.sin(a)))));
        for (int i = 0; i < info.size(); i++) {
            g.drawString(info.get(i), sx, sY+(spacing*i));
        }
        int cw = 400, ch = 150;
        Vector2 sv = new Vector2(cX-cw/2, cY-ch-ch/2);
        g.setColor(Color.decode("#eeeeee"));
        g.fillRect(sv.x(), sv.y(), cw, ch);
        g.setColor(Color.decode("#000000"));
        int points = 400;
        int every = cw/points;
        Vector2 bef = null;
        ArrayList<Float> last = new ArrayList<>();
        for (int x = 0; x < points; x++) {
            double t = getAtLast(x);
            int xpos = every*x;
            if(t != -999){
                addLimit(last, t, 1);
                int fx = sv.x()+xpos;
                int fy =(int)MathUtils.map(MathUtils.getAverageValue(last), 360, 0, sv.y(), sv.y()+ch);
                if(bef == null) {
                    bef = new Vector2(fx, fy);
                }
                g.drawLine(bef.x(), bef.y(), fx, fy);
                bef = new Vector2(fx, fy);
            }
        }

        // рисуем линию курсора
        g.setColor(Color.RED);
        g.drawLine(cX, cY, mouse.x(), mouse.y());
        g.drawRect(s.x(), s.y(), 6, 6);
        String angleText = Math.round((float)angle)+"°";
        Rectangle2D metrics = font.getStringBounds(angleText, g.getFontRenderContext());
        int width = (int) metrics.getWidth();
        Vector2 pos = mouse.clone().add(-(width/2), (int) -(metrics.getHeight()-15));
        g.drawString(angleText, pos.x(), pos.y());


        g.setColor(Color.decode("#00aa00"));
        g.setFont(f3);
        String fpsString = fps+" FPS";
        String emsString = tim.total()+"ms render time";
        int offset = (int) f3.getStringBounds(fpsString, g.getFontRenderContext()).getHeight();
        g.drawString(fpsString, 0, offset);
        offset += (int) f3.getStringBounds(emsString, g.getFontRenderContext()).getHeight();
        g.drawString(emsString, 0, offset);
        updateFps();
    }

    @SuppressWarnings("SameParameterValue")
    private void addLimit(ArrayList<Float> list, double value, int limit) {
        if(limit <= list.size()){
            list.removeFirst();
        }
        list.add((float)value);
    }

    private double getAtLast(int sub){
        Long[] d = angles.keySet().toArray(new Long[0]);
        ArrayUtils.reverse(d);
        for (Long l : d) {
            if(sub == 0)return angles.get(l);
            sub--;
        }
        return -999;
    }
    private void putCache(double angle){
        angles.put(System.currentTimeMillis(), angle);
        clear();
    }
    private void clear(){
        ArrayList<Long> queue = new ArrayList<>();
        for (Long l : angles.keySet()) {
            if(System.currentTimeMillis() - l >= 100_000){
                queue.add(l);
            }
        }
        for (Long l : queue) {
            angles.remove(l);
        }
    }

    public static void updateFps() {
        long currentTime = System.currentTimeMillis();
        frameCount++;

        if (currentTime - lastTime >= 50) {
            fps = (int) (frameCount / ((currentTime - lastTime) / 1000.0));
            frameCount = 0;
            lastTime = currentTime;
        }
    }
}
