package rasterize;

import model.Point;
import model.Polygon;

public class PolygonRasterizer {
    private LineRasterizer lineRasterizer;

    public PolygonRasterizer(LineRasterizer lineRasterizer) {
        this.lineRasterizer = lineRasterizer;
    }

    public void setLineRasterizer(LineRasterizer lineRasterizer) {
        this.lineRasterizer = lineRasterizer;
    }

    public void rasterize(Polygon polygon) {

        if (polygon.getPoints().size() < 3) return;

        for (int i = 0; i < polygon.getPoints().size(); i++) {
            int indexA = i;
            int indexB = (i + 1) % polygon.getPoints().size();

            var a = polygon.getPoint(indexA);
            var b = polygon.getPoint(indexB);

            lineRasterizer.rasterize(a.getX(), a.getY(), b.getX(), b.getY());


        }
    }
}
