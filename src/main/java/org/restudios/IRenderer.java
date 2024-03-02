package org.restudios;

import org.ReStudios.utitlitium.vectors.Vector2;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public interface IRenderer {
    void render(Graphics2D g, @Nullable Vector2 mouse, Vector2 size);
}
