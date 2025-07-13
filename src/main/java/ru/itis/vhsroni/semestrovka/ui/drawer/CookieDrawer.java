package ru.itis.vhsroni.semestrovka.ui.drawer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;

public class CookieDrawer {

    public static void drawCookie(GraphicsContext gc, double x, double y, double width, double height) {
        RadialGradient cookieGradient = new RadialGradient(
                0, 0, x + width / 2, y + height / 2, width / 2, false, CycleMethod.NO_CYCLE,
                new Stop(0, Color.BISQUE),
                new Stop(1, Color.SADDLEBROWN)
        );

        gc.setFill(cookieGradient);
        gc.fillOval(x, y, width, height);

        gc.setStroke(Color.SIENNA);
        gc.setLineWidth(width * 0.05);
        gc.strokeOval(x + width * 0.025, y + height * 0.025, width * 0.95, height * 0.95);

        RadialGradient highlight = new RadialGradient(
                0, 0, x + width * 0.35, y + height * 0.35, width * 0.3, false, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(255, 255, 255, 0.6)),
                new Stop(1, Color.TRANSPARENT)
        );
        gc.setFill(highlight);
        gc.fillOval(x + width * 0.1, y + height * 0.1, width * 0.8, height * 0.8);

        double[][] chips = {
                {x + width * 0.3, y + height * 0.3},
                {x + width * 0.5, y + height * 0.2},
                {x + width * 0.7, y + height * 0.4},
                {x + width * 0.3, y + height * 0.6},
                {x + width * 0.6, y + height * 0.7}
        };
        for (double[] chip : chips) {
            RadialGradient chipGradient = new RadialGradient(
                    0, 0, chip[0] + width * 0.09, chip[1] + height * 0.09, width * 0.09, false,
                    CycleMethod.NO_CYCLE,
                    new Stop(0, Color.BLACK),
                    new Stop(1, Color.SADDLEBROWN)
            );
            gc.setFill(chipGradient);
            gc.fillOval(chip[0], chip[1], width * 0.18, height * 0.18);

            RadialGradient chipHighlight = new RadialGradient(
                    0, 0, chip[0] + width * 0.06, chip[1] + height * 0.06, width * 0.06, false,
                    CycleMethod.NO_CYCLE,
                    new Stop(0, Color.rgb(255, 255, 255, 0.6)),
                    new Stop(1, Color.TRANSPARENT)
            );
            gc.setFill(chipHighlight);
            gc.fillOval(chip[0] + width * 0.03, chip[1] + height * 0.03, width * 0.12, height * 0.12);
        }
    }
}