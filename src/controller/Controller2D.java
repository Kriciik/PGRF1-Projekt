package controller;


import clip.Clipper;
import fill.Filler;
import fill.ScanLineFiller;
import fill.SeedfillFiller;
import model.Point;
import model.Polygon;
import model.Rectangle;
import rasterize.LineRasterizer;
import rasterize.LineRasterizerTrivial;
import rasterize.PolygonRasterizer;
import view.Panel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Controller2D {

    private final Panel panel;

    // Nastavení barev pro vykreslování
    final private Color color1 = Color.BLUE;
    final private Color color2 = Color.PINK;

    private final LineRasterizer lineRasterizer;
    private final PolygonRasterizer polygonRasterizer;

    private Filler filler;
    private Point seedFillStart;

    private Polygon polygon = new Polygon();
    private Polygon polygonClipper = new Polygon();

    private Point rectA;
    private Point rectB;
    private Polygon rectangle;

    private Clipper clipper;

    public Controller2D(Panel panel) {
        this.panel = panel;

        // DDA Algoritmus
        lineRasterizer = new LineRasterizerTrivial(panel.getRaster());
        lineRasterizer.setColor(color1);

        polygonRasterizer = new PolygonRasterizer(lineRasterizer);

        initListeners();

    }

    private void initListeners() {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                // Omezení kliknutí mimo raster
                if (e.getX() < 0 || e.getX() >= panel.getRaster().getWidth() ||
                        e.getY() < 0 || e.getY() >= panel.getRaster().getHeight()) {
                    return;
                }
                Point p = new Point(e.getX(), e.getY());
                if (e.isShiftDown() && e.getButton() == MouseEvent.BUTTON1) {

                    if (rectA == null) {
                        rectA = p;
                        return;
                    }

                    if (rectB == null) {
                        rectB = p;
                        return;
                    }

                    // třetí bod -> vytvoříme obdélník
                    rectangle = new Rectangle(rectA, rectB, p);
                    // vyprázdníme stav
                    rectA = null;
                    rectB = null;

                    drawScene();
                    return;
                }
                // vykreslení
                if (e.getButton() == MouseEvent.BUTTON1) {

                    Point point = new Point( e.getX(), e.getY() );
                    polygon.addPoint(point);

                    drawScene();

                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    Point newPoint = new Point(e.getX(), e.getY());

                    if (isConvexWithNewPoint(polygonClipper, newPoint)) {
                        polygonClipper.addPoint(newPoint);

                        if (polygonClipper.getPoints().size() >= 3) drawScene();
                        else panel.repaint();

                    }
                }

               if(SwingUtilities.isMiddleMouseButton(e)) {
                    seedFillStart = new Point(e.getX(), e.getY());
                    filler = new SeedfillFiller(panel.getRaster(),
                            seedFillStart.getX(), seedFillStart.getY(),
                            new Color(color2.getRGB()));
                    drawScene();
                    filler.fill();
                }
            }
        });

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_C) {
                    clearScene();
                }
            }
        });

    }
    private void drawScene(){

        panel.getRaster().clear();

        polygonRasterizer.rasterize(polygon);
        polygonRasterizer.rasterize(polygonClipper);

        if (rectangle != null) {
            polygonRasterizer.rasterize(rectangle);
        }

        if (polygonClipper.getPoints().size() < 3) {
            Filler scanLine = new ScanLineFiller(lineRasterizer, polygonRasterizer, polygon);

            scanLine.fill();
        } else if(polygon.getPoints().size() >= 3 && polygonClipper.getPoints().size() >= 3){
            clipper = new Clipper();
            ArrayList<Point> clippedPoints = clipper.clip(polygonClipper.getPoints(), polygon.getPoints());

            if (clippedPoints.size() >= 3) {
                Polygon newPolygon = new Polygon(clippedPoints);
                Filler scanLine = new ScanLineFiller(lineRasterizer, polygonRasterizer, newPolygon);

                scanLine.fill();
            }
        }


        panel.repaint();
    }

    private void clearScene(){
        polygon = new Polygon();
        polygonClipper = new Polygon();
        rectangle = new Polygon();

        polygon.getPoints().clear();
        polygonClipper.getPoints().clear();
        rectangle.getPoints().clear();

        drawScene();
    }

    private boolean isConvexWithNewPoint(Polygon polygon, Point newPoint) {
        ArrayList<Point> points = new ArrayList<>(polygon.getPoints());
        points.add(newPoint);

        if (points.size() < 3) return true; // 1–2 body → vždy konvexní

        boolean gotPositive = false;
        boolean gotNegative = false;
        int n = points.size();

        for (int i = 0; i < n; i++) {
            Point A = points.get(i);
            Point B = points.get((i + 1) % n);
            Point C = points.get((i + 2) % n);

            int cross = (B.getX() - A.getX()) * (C.getY() - B.getY())
                    - (B.getY() - A.getY()) * (C.getX() - B.getX());

            if (cross > 0) gotPositive = true;
            else if (cross < 0) gotNegative = true;

            if (gotPositive && gotNegative) return false; // nekonvexní
        }

        return true;
    }

}
