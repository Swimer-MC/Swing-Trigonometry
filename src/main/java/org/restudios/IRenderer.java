package org.restudios;

import org.ReStudios.utitlitium.vectors.Vector2;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public interface IRenderer {
    void render(Graphics2D g, @Nullable Vector2 mouse, Vector2 size);
    default void keyRelease(KeyEvent e){}
}
