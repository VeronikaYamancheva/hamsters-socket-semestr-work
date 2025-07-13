package ru.itis.vhsroni.semestrovka.ui.drawer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class PixelDoorDrawer {

    private static final Color[] COLORS = {
            Color.rgb(51, 25, 0),
            Color.rgb(77, 38, 0),
            Color.rgb(102, 51, 0),
            Color.rgb(128, 64, 0),
            Color.rgb(102, 68, 34),
            Color.rgb(89, 55, 34),
            Color.rgb(69, 34, 0),
            Color.rgb(92, 64, 51),
            Color.rgb(77, 51, 25)
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