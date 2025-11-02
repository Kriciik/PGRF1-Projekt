package fill;

import model.Edge;
import model.Point;
import model.Polygon;
import rasterize.LineRasterizer;
import rasterize.PolygonRasterizer;

import java.util.ArrayList;
import java.util.Collections;

public class ScanLineFiller implements Filler {
    private LineRasterizer lineRasterizer;
    private PolygonRasterizer polygonRasterizer;
    private Polygon polygon;

    public ScanLineFiller(LineRasterizer lineRasterizer, PolygonRasterizer polygonRasterizer, Polygon polygon) {
        this.lineRasterizer = lineRasterizer;
        this.polygonRasterizer = polygonRasterizer;
        this.polygon = polygon;
    }

    @Override
    public void fill() {
        // TODO: nechci vyplnit polygon, který má méně, jak 3 vrcholy DONE
        if (polygon.getPoints().size() < 3) {
            return;
        }

        // seznam hran
        ArrayList<Edge> edges = new ArrayList<>();

        for (int i = 0; i < polygon.getPoints().size(); i++) {
            int indexA = i;
            int indexB = i + 1;
            if (indexB == polygon.getPoints().size())
                indexB = 0;

            Point a = polygon.getPoint(indexA);
            Point b = polygon.getPoint(indexB);

            Edge edge = new Edge(a, b);
            if (!edge.isHorizontal()) {
                edge.orientate();
                edges.add(edge);
            }
        }

        // TODO: Najít yMin a yMax DONE
        int yMin = edges.getFirst().getY1();
        int yMax = edges.getFirst().getY2();

        // TODO: projít všechny pointy polygonu a najít min a max DONE
        for (Edge edge : edges) {
            if (edge.getY1() < yMin) yMin = edge.getY1();
            if (edge.getY2() > yMax) yMax = edge.getY2();
        }


        for (int y = yMin; y <= yMax; y++) {
            ArrayList<Integer> intersections = new ArrayList<>();

            // Najdi všechny průsečíky s hranami
            for (Edge edge : edges) {
                if (!edge.isIntersection(y))
                    continue;
                int x = edge.getIntersection(y);
                intersections.add(x);
            }
            // Sorting (nejmenší - největší)
            Collections.sort(intersections);


            // TODO: Spojím (obarvím) průsečíky, 0 - 1, 2 - 3, 4 - 5, 6 - 7 DONE
            for (int i = 0; i < intersections.size() - 1; i += 2) {
                int xStart = intersections.get(i);
                int xEnd = intersections.get(i + 1);

                // vykresli úsečku mezi nimi
                lineRasterizer.rasterize(xStart, y, xEnd, y);
            }

        }
        // TODO: Vykreslím hranici polygonu DONE
        polygonRasterizer.rasterize(polygon);
    }
}
