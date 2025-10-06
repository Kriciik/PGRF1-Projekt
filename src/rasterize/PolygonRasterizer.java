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
        Point point1;
        Point point2;

            for(int i = 0; i < polygon.getPoints().size(); i++) {
                point1 = polygon.getPoints().get(i);

                if(point1 == polygon.getPoint(polygon.getPoints().size() - 1)) {
                    point2 =  polygon.getPoints().getFirst();

                }else{
                    point2 =  polygon.getPoints().get(i+1);
                }

                polygon.addPoint(point1);
                polygon.addPoint(point2);

                if(polygon.getPoints().size() < 3) {
                    lineRasterizer.rasterize(point1.getX(), point1.getY(), point2.getX(), point2.getY());
                }


        }
    }
}
