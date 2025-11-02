package fill;

import raster.Raster;

import java.awt.*;
import java.util.OptionalInt;


public class SeedfillFiller implements Filler {

    private Raster raster;
    private int startX;
    private int startY;

    private Color fillColor;
    private Color backgroundColor;

    public SeedfillFiller(Raster raster, int startX, int startY, Color fillColor) {
        this.raster = raster;
        this.startX = startX;
        this.startY = startY;
        this.fillColor = fillColor;
        OptionalInt pixelColor = raster.getPixel(startX, startY);
        if(pixelColor.isPresent()) {
            this.backgroundColor = new Color(pixelColor.getAsInt());
        }

    }

    @Override
    public void fill() {
        seedFill(startX, startY);
    }

    private void seedFill(int x, int y) {
        OptionalInt pixelColor = raster.getPixel(x, y);
        if(pixelColor.isEmpty()) {
            return;
        }

        if(pixelColor.getAsInt() != backgroundColor.getRGB()) {
            return;
        }
        if(pixelColor.getAsInt() == fillColor.getRGB()) {
            return;
        }

        raster.setPixel(x, y, fillColor);

        seedFill(x+1, y);
        seedFill(x, y+1);
        seedFill(x-1, y);
        seedFill(x, y-1);

    }
}
