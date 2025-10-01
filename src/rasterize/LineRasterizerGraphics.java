package rasterize;

import java.awt.*;

public class LineRasterizerGraphics extends LineRasterizer {

    public LineRasterizerGraphics(RasterBufferedImage raster) {
        super(raster);
    }

    @Override
    public void rasterize(int x1, int y1, int x2, int y2, int color) {
        Graphics g = raster.getImage().getGraphics();
        g.setColor(Color.BLUE); // TODO: barva
        g.drawLine(x1, y1, x2, y2);
    }
}
