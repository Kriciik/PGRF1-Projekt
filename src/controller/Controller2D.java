package controller;


import model.Line;
import model.Point;
import model.Polygon;
import rasterize.LineRasterizer;
import rasterize.LineRasterizerColorTransition;
import rasterize.LineRasterizerGraphics;
import rasterize.LineRasterizerTrivial;
import view.Panel;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Controller2D {
    // #TODO LINEÁRNÍ INTERPOLACE: NAJDI SI SLIDE Z PREZENTACE A NAUČ SE TO BRO

    private final Panel panel;
    private int color = 0xffffff;
    private LineRasterizer lineRasterizer;

    private ArrayList<Line> lines = new ArrayList<>();
    private Point point;
    private Polygon polygon = new Polygon();

    private int lineStartX, lineStartY;
    private boolean isLineDrawing = false;

    public Controller2D(Panel panel) {
        this.panel = panel;

        lineRasterizer = new LineRasterizerTrivial(panel.getRaster());
        //lineRasterizer = new LineRasterizerColorTransition(panel.getRaster());
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
                if (SwingUtilities.isLeftMouseButton(e)) {
                    // přidáme bod do polygonu
                    Point point = new Point( e.getX() ,  e.getY() );
                    polygon.addPoint(point);
                    drawScene();
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    // začátek úsečky
                    lineStartX =  e.getX() ;
                    lineStartY =  e.getY() ;
                    isLineDrawing = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e) && isLineDrawing) {
                    int x = e.getX();
                    int y = e.getY();

                    // omezení
                    x = Math.max(0, Math.min(x, panel.getRaster().getWidth() - 1));
                    y = Math.max(0, Math.min(y, panel.getRaster().getHeight() - 1));

                    // vykreslíme úsečku
                    lineRasterizer.rasterize(lineStartX, lineStartY, x, y);

                    isLineDrawing = false;
                    panel.repaint();
                }
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {

                //preview
                if (isLineDrawing) {
                    int x = e.getX();
                    int y = e.getY();

                    // Omezení kliknutí mimo raster
                    x = Math.max(0, Math.min(x, panel.getRaster().getWidth() - 1));
                    y = Math.max(0, Math.min(y, panel.getRaster().getHeight() - 1));

                    drawScene();

                    lineRasterizer.rasterize(lineStartX, lineStartY, x, y);

                    panel.repaint();
                }
            }
        });

        panel.addKeyListener(new KeyAdapter() {
            // změna barev #TODO nefunguje (zatím)
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

        panel.getRaster().clear();

        // Pokud aspoň 2 body, vykreslí se
        if (polygon.getPoints().size() >= 2) {
            for (int i = 0; i < polygon.getPoints().size() - 1; i++) {
                var p1 = polygon.getPoint(i);
                var p2 = polygon.getPoint(i + 1);
                lineRasterizer.rasterize(p1.getX(), p1.getY(), p2.getX(), p2.getY());
            }
        }
        // Spojení Prvního a posledního bodu
        if (polygon.getPoints().size() > 2) {
            var p1 = polygon.getPoint(0);
            var p2 = polygon.getPoint(polygon.getPoints().size() - 1);
            lineRasterizer.rasterize(p1.getX(), p1.getY(), p2.getX(), p2.getY());
        }

        panel.repaint();
    }
}
