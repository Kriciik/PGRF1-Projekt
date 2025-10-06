package controller;


import model.Line;
import model.Point;
import model.Polygon;
import rasterize.LineRasterizer;
import rasterize.LineRasterizerGraphics;
import rasterize.LineRasterizerTrivial;
import view.Panel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Controller2D {
    private final Panel panel;
    private int color = 0xffffff;
    private LineRasterizer lineRasterizer;

    private ArrayList<Line> lines = new ArrayList<>();
    private Point point;
    private Polygon polygon = new Polygon();
    private int startX,startY;
    boolean isLineStartSet = false;

    public Controller2D(Panel panel) {
        this.panel = panel;

        lineRasterizer = new LineRasterizerTrivial(panel.getRaster());

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
                // vykreslení
                point = new Point(e.getX(), e.getY());
                polygon.addPoint(point);

                drawScene();
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // vykresli úsečku od - do
                if(e.getX() < 0 || e.getX() > panel.getRaster().getWidth() - 1|| e.getY() < 0 || e.getY() > panel.getRaster().getHeight() - 1) {
                   return;
                }
                    int centerX = panel.getRaster().getWidth() / 2;
                    int centerY = panel.getRaster().getHeight() / 2;

            }
        });

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_O) {
                    color = 0xff0000;
                }

                if(e.getKeyCode() == KeyEvent.VK_P) {
                    color = 0xffffff;
                }
                if(e.getKeyCode() == KeyEvent.VK_SPACE) {
                    color = 16776985;
                }
            }
        });

    }
    private void drawScene(){

        // nejdřív vyčisti raster
        panel.getRaster().clear();

        // pokud má polygon alespoň 2 body, vykresli jeho hrany
        if (polygon.getPoints().size() >= 2) {
            for (int i = 0; i < polygon.getPoints().size() - 1; i++) {
                var p1 = polygon.getPoint(i);
                var p2 = polygon.getPoint(i + 1);
                lineRasterizer.rasterize(p1.getX(), p1.getY(), p2.getX(), p2.getY());
            }
        }

        if (polygon.getPoints().size() > 2) {
            var p1 = polygon.getPoint(0);
            var p2 = polygon.getPoint(polygon.getPoints().size() - 1);
            lineRasterizer.rasterize(p1.getX(), p1.getY(), p2.getX(), p2.getY());
        }

        panel.repaint();
    }
}
