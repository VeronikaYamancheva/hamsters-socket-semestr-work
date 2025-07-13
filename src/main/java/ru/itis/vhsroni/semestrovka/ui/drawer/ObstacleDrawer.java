package ru.itis.vhsroni.semestrovka.ui.drawer;

import javafx.scene.canvas.GraphicsContext;
import ru.itis.vhsroni.semestrovka.settings.UIConstants;

public class ObstacleDrawer {

    public static void drawObstacle(GraphicsContext graphicsContext, int row, int col, double tileWidth, double tileHeight) {
        graphicsContext.setFill(UIConstants.OBSTACLE_LIGHT_BROWN_COLOR);
        graphicsContext.fillRect(col * tileWidth, row * tileHeight, tileWidth, tileHeight);
        graphicsContext.setStroke(UIConstants.OBSTACLE_DARK_BROWN_COLOR);
        graphicsContext.setLineWidth(2);
        for (int i = 0; i < 4; i++) {
            double y = row * tileHeight + i * (tileHeight / 4);
            graphicsContext.strokeLine(col * tileWidth, y, (col + 1) * tileWidth, y);
        }

    }
}
