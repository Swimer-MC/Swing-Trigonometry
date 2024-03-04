package org.restudios;

import org.ReStudios.utitlitium.FPSCalculator;
import org.ReStudios.utitlitium.NFile;
import org.ReStudios.utitlitium.ThreadBuilder;
import org.ReStudios.utitlitium.Timer;
import org.ReStudios.utitlitium.vectors.Vector2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Frame extends JFrame {
    IRenderer renderer;
    public static Font f3;
    static {
        try {
            f3 = Font.createFonts(new NFile("Ticketing.ttf"))[0].deriveFont(Font.PLAIN, 22);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Frame(IRenderer renderer){
        super("So[Co]sinus - "+renderer.getClass().getSimpleName()+".java");
        this. renderer = renderer;
        FPSCalculator fpsMgr = new FPSCalculator();
        JPanel canvas = new JPanel(){
            Vector2 before = new Vector2(0,0);
            @Override
            public void paint(Graphics g) {
                fpsMgr.updateFps();
                Timer tim = new Timer();
                tim.run();
                Point m = getMousePosition();
                Vector2 mouse = m == null ? before : new Vector2(m.x, m.y);
                g.setColor(Color.WHITE);
                g.clearRect(0, 0, getWidth(), getHeight());
                renderer.render((Graphics2D) g, mouse, new Vector2(getWidth(), getHeight()));

                before = mouse;

                g.setFont(f3);
                String fpsString = fpsMgr.averageFPS()+" FPS";
                String emsString = tim.total()+"ms render time";

                Rectangle2D fpsOffset = f3.getStringBounds(fpsString, ((Graphics2D)g).getFontRenderContext());
                Rectangle2D emsOffset = f3.getStringBounds(emsString, ((Graphics2D)g).getFontRenderContext());
                int height = (int) (fpsOffset.getHeight() + emsOffset.getHeight());
                int width = (int) Math.max(fpsOffset.getWidth(), emsOffset.getWidth());
                g.setColor(new Color(160, 160, 160, 160));
                g.fillRect(0, 0, width+10, height+10);
                g.setColor(Color.decode("#ffffff"));
                g.drawString(fpsString, 5, (int) fpsOffset.getHeight());
                g.drawString(emsString, 5, (int) fpsOffset.getHeight() + (int)emsOffset.getHeight());
            }
        };
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                renderer.keyRelease(e);
            }
        });

        setContentPane(canvas);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setVisible(true);
        new ThreadBuilder(() -> {
            try {
                long passed;
                //noinspection InfiniteLoopStatement
                while (true){
                    passed = System.currentTimeMillis();
                    SwingUtilities.invokeLater(canvas::repaint);
                    passed = System.currentTimeMillis() - passed;
                    TimeUnit.MILLISECONDS.sleep(1000/60-passed);
                }
            }catch (Throwable t){
                t.printStackTrace(System.out);
            }
        }).buildAndRun();
    }
}
