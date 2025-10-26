package controller;


import fill.Filler;
import fill.SeedfillFiller;
import model.Line;
import model.Point;
import model.Polygon;
import rasterize.LineRasterizer;
import rasterize.LineRasterizerColorTransition;
import rasterize.LineRasterizerTrivial;
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
    final private Color color2 = Color.GRAY;

    private LineRasterizer lineRasterizer;

    private Filler  filler;
    private Point seedFillStart;

    private Polygon polygon = new Polygon();

    private int lineStartX, lineStartY;
    private boolean isLineDrawing = false;

    public Controller2D(Panel panel) {
        this.panel = panel;

        // DDA Algoritmus
        lineRasterizer = new LineRasterizerTrivial(panel.getRaster());
        lineRasterizer.setColor(Color.BLUE);


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
                if (e.getButton() == MouseEvent.BUTTON1) {
                    // přidám bod do polygonu
                    Point point = new Point( e.getX(), e.getY() );
                    polygon.addPoint(point);
                    drawScene();
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    // začátek úsečky
                    lineStartX =  e.getX();
                    lineStartY =  e.getY();
                    isLineDrawing = true;
                }

                if(SwingUtilities.isMiddleMouseButton(e)) {

                    seedFillStart = new Point(e.getX(), e.getY());
                    filler = new SeedfillFiller(panel.getRaster(),
                            seedFillStart.getX(), seedFillStart.getY(),
                            new Color(0xFF3366));

                    drawScene();
                    filler.fill();
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

                    isLineDrawing = false;
                    panel.repaint();
                }

            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {

                if (isLineDrawing) {
                    int x = e.getX();
                    int y = e.getY();

                    // Omezíme kliknutí mimo raster
                    x = Math.max(0, Math.min(x, panel.getRaster().getWidth() - 1));
                    y = Math.max(0, Math.min(y, panel.getRaster().getHeight() - 1));

                    // SNAP TO AXIS — pokud je SHIFT stisknutý
                    if (e.isShiftDown()) {
                        int dx = x - lineStartX;
                        int dy = y - lineStartY;

                        if (Math.abs(dx) > Math.abs(dy) * 2) {
                            // Vodorovná
                            y = lineStartY;
                        } else if (Math.abs(dy) > Math.abs(dx) * 2) {
                            // Svislá
                            x = lineStartX;
                        } else {
                            // Úhlopříčná
                            int signX = (dx >= 0) ? 1 : -1;
                            int signY = (dy >= 0) ? 1 : -1;
                            int len = Math.min(Math.abs(dx), Math.abs(dy));
                            x = lineStartX + len * signX;
                            y = lineStartY + len * signY;
                        }
                    }

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

                if(e.getKeyCode() == KeyEvent.VK_CONTROL) {

                    lineRasterizer = new LineRasterizerTrivial(panel.getRaster());
                    drawScene();
                }
                if(e.getKeyCode() == KeyEvent.VK_SPACE) {

                    lineRasterizer = new LineRasterizerColorTransition(panel.getRaster());
                    lineRasterizer.setColors(color1, color2);
                    drawScene();
                }

                if(e.getKeyCode() == KeyEvent.VK_C) {
                    clearScene();
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

    private void clearScene(){
        polygon = new Polygon();
        polygon.getPoints().clear();
        drawScene();
    }

    private Point calculateSnap(int x, int y, boolean shiftDown) {
        if (!shiftDown) return new Point(x, y);

        int dx = x - lineStartX;
        int dy = y - lineStartY;

        if (Math.abs(dx) > Math.abs(dy) * 2) {
            y = lineStartY; // horizontální
        } else if (Math.abs(dy) > Math.abs(dx) * 2) {
            x = lineStartX; // vertikální
        } else {
            int signX = dx >= 0 ? 1 : -1;
            int signY = dy >= 0 ? 1 : -1;
            int len = Math.min(Math.abs(dx), Math.abs(dy));
            x = lineStartX + len * signX;
            y = lineStartY + len * signY;
        }
        return new Point(x, y);
    }
}
