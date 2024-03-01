package org.restudios;

import org.ReStudios.utitlitium.ThreadBuilder;
import org.ReStudios.utitlitium.vectors.Vector2;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

public class Frame extends JFrame {
    Renderer renderer;
    public Frame(){
        super("So[Co]sinus");
        renderer = new Renderer();
        JPanel canvas = new JPanel(){
            @Override
            public void paint(Graphics g) {
                Point m = getMousePosition();
                g.setColor(Color.WHITE);
                g.clearRect(0, 0, getWidth(), getHeight());
                renderer.render((Graphics2D) g, m == null ? null : new Vector2(m.x, m.y), new Vector2(getWidth(), getHeight()));
            }
        };

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
