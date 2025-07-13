package ru.itis.vhsroni.semestrovka.ui.animation;

import javafx.scene.canvas.GraphicsContext;
import ru.itis.vhsroni.semestrovka.settings.UIConstants;

public class StarAnimation {
    private int x;
    private int y;
    private double rotationAngle;
    private long startTime;
    private boolean isActive;

    public StarAnimation(int x, int y) {
        this.x = x;
        this.y = y;
        this.rotationAngle = 0;
        this.isActive = false;
    }

    public void start() {
        this.startTime = System.nanoTime();
        this.isActive = true;
    }

    public void update() {
        if (!isActive) return;

        long currentTime = System.nanoTime();
        long elapsedTime = (currentTime - startTime) / 1_000_000;

        if (elapsedTime >= 1000) {
            isActive = false;
            return;
        }


        rotationAngle = (elapsedTime / 1000.0) * 360;
    }

    public void draw(GraphicsContext gc, double tileWidth, double tileHeight) {
        if (!isActive) return;
        gc.save();
        gc.translate(x * tileWidth + tileWidth / 2, y * tileHeight + tileHeight / 2);
        gc.rotate(rotationAngle);

        drawStar(gc, -tileWidth / 2, -tileHeight / 2, tileWidth, tileHeight);
        gc.restore();
    }

    private void drawStar(GraphicsContext gc, double x, double y, double width, double height) {
        gc.setFill(UIConstants.STAR_ANIMATION_COLOR);

        double[] xPoints = {x + width * 0.5, x + width * 0.6, x + width * 0.8, x + width * 0.65, x + width * 0.7, x + width * 0.5, x + width * 0.3, x + width * 0.35, x + width * 0.2, x + width * 0.4};
        double[] yPoints = {y, y + height * 0.35, y + height * 0.35, y + height * 0.55, y + height * 0.9, y + height * 0.7, y + height * 0.9, y + height * 0.55, y + height * 0.35, y + height * 0.35};
        gc.fillPolygon(xPoints, yPoints, 10);
    }

    public boolean isActive() {
        return isActive;
    }
}