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
import solids.Arrow;
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
    private LineRasterizer lineRasterizer;

    private Solid arrow;
    private Solid cube;
    public Controller3D(Panel panel) {
        this.panel = panel;
        this.lineRasterizer = new LineRasterizerTrivial(panel.getRaster());
        this.renderer = new Renderer(lineRasterizer);

        cube = new Cube();
        arrow = new Arrow();
        initListeners();
    }

    private void initListeners() {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                drawScene();
            }
        });
    }

    private void drawScene() {

        renderer.render(arrow);
        renderer.render(cube);
        panel.repaint();
    }

    private void clearScene() {
        drawScene();
    }


}


