package controller;

import rasterize.LineRasterizer;
import rasterize.LineRasterizerTrivial;
import render.Renderer;
import solids.*;
import transforms.*;
import view.Panel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Controller3D {
    private final Panel panel;
    private final Renderer renderer;
    private LineRasterizer lineRasterizer;

    // Solids
    private Solid arrow;
    private Solid cube;
    private Solid axisX, axisY, axisZ;

    // Camera
    private Camera camera;
    private Mat4 proj;

    public Controller3D(Panel panel) {
        this.panel = panel;
        this.lineRasterizer = new LineRasterizerTrivial(panel.getRaster());
        camera = new Camera()
                .withPosition(new Vec3D(0.4, -1.5, 1))
                .withAzimuth(Math.toRadians(90)) // levá - pravá
                .withZenith(Math.toRadians(-25)) // nahoru - dolů
                .withFirstPerson(true);
        proj = new Mat4PerspRH(Math.toRadians(90),
                panel.getRaster().getHeight() / (double)panel.getRaster().getWidth(),
                0.1,
                100);
        this.renderer = new Renderer(
                lineRasterizer,
                panel.getRaster().getWidth(),
                panel.getRaster().getHeight(),
                camera.getViewMatrix(),
                proj
        );

        cube = new Cube();
        arrow = new Arrow();

        axisX = new AxisX();
        axisY = new AxisY();
        axisZ = new AxisZ();

        initListeners();
    }


    private void initListeners() {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                drawScene();
            }
        });

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    arrow.setModel(new Mat4Transl(0.5, 0,0));

                }
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    arrow.setModel(new  Mat4Transl(-0.5, 0,0)
                            .mul(new Mat4RotZ(Math.toRadians(15)))
                            .mul(new Mat4Transl(0.5, 0,0)));

                }
                if(e.getKeyCode() == KeyEvent.VK_C) {
                    clearScene();
                }

                if (e.getKeyCode() == KeyEvent.VK_A) {
                    camera = camera.left(0.5);
                }
                if (e.getKeyCode() == KeyEvent.VK_D) {
                    camera = camera.right(0.5);
                }
                if (e.getKeyCode() == KeyEvent.VK_W) {
                    camera = camera.forward(0.5);
                }
                if (e.getKeyCode() == KeyEvent.VK_S) {
                    camera = camera.backward(0.5);
                }
                drawScene();

            }
        });
    }


    private void drawScene() {
        // clear rasteru
        panel.getRaster().clear();
        //nastavení camery
        renderer.setView(camera.getViewMatrix());
        // renderovaní modelů
        renderer.render(arrow);
        renderer.render(axisX);
        renderer.render(axisY);
        renderer.render(axisZ);

        panel.repaint();
    }


    private void clearScene() {
        drawScene();
    }


}


