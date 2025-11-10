package controller;

import clip.Clipper;
import fill.Filler;
import fill.ScanLineFiller;
import model.Polygon;
import rasterize.LineRasterizer;
import rasterize.LineRasterizerColorTransition;
import rasterize.LineRasterizerTrivial;
import rasterize.PolygonRasterizer;
import render.Renderer;
import solids.Cube;
import solids.Solid;
import view.Panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Controller3D {
        private final Panel panel;
        private final Renderer renderer;

        private Solid cube;

        public Controller3D(Panel panel) {
            this.panel = panel;
            this.renderer = new Renderer();

            cube = new Cube();
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
                        drawScene();

                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        drawScene();
                    }

                    if(SwingUtilities.isMiddleMouseButton(e)) {
                        drawScene();

                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }
            });

            panel.addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {

                        drawScene();
                        panel.repaint();
                }
            });

            panel.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {

                    if(e.getKeyCode() == KeyEvent.VK_CONTROL) {
                        drawScene();
                    }
                    if(e.getKeyCode() == KeyEvent.VK_SPACE) {
                        drawScene();
                    }

                    if(e.getKeyCode() == KeyEvent.VK_C) {
                        clearScene();
                    }
                }
            });

        }
        private void drawScene(){

            renderer.render(cube);

            panel.repaint();
        }

        private void clearScene(){
            drawScene();
        }


}


