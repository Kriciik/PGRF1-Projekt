package rasterize;

import model.Line;
import raster.RasterBufferedImage;

import java.awt.*;

public abstract class LineRasterizer {
    protected RasterBufferedImage raster;

    protected Color c1 = Color.WHITE;
    protected Color c2 = Color.BLACK;

    public LineRasterizer(RasterBufferedImage raster) {
        this.raster = raster;

    }

    public void setColor(Color color) {
        this.c1 = color;
    }

    public void setColors(Color color1, Color color2) {
        this.c1 = color1;
        this.c2 = color2;
    }
    public void rasterize(int x1, int y1, int x2, int y2) {

    }

    public void rasterize(Line line) {
        rasterize(line.getX1(), line.getY1(), line.getX2(), line.getY2());
    }
}
