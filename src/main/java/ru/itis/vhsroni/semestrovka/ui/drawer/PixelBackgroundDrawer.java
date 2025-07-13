package ru.itis.vhsroni.semestrovka.ui.drawer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class PixelBackgroundDrawer {

    private static final Color[] COLORS = {
            Color.rgb(240, 186, 255),
            Color.rgb(202, 186, 255),
            Color.rgb(250, 200, 255),
            Color.rgb(220, 186, 255),
            Color.rgb(240, 186, 235),
            Color.rgb(250, 186, 255),
            Color.rgb(240, 156, 255),
            Color.rgb(250, 176, 255),
            Color.rgb(255, 196, 255),
    };

    public static void drawPixelBackground(GraphicsContext gc, double x, double y, double width, double height) {
        double partWidth = width / 3;
        double partHeight = height / 3;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Color color = COLORS[(row * 3 + col) % COLORS.length];
                gc.setFill(color);

                gc.fillRect(
                        x + col * partWidth,
                        y + row * partHeight,
                        partWidth,
                        partHeight
                );
            }
        }
    }
}