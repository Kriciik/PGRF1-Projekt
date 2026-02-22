package controller;

import model.Vertex;
import raster.TriangleRasterizerTest;
import raster.ZBuffer;
import rasterize.LineRasterizer;
import rasterize.LineRasterizerTrivial;
import render.Renderer;
import solids.*;
import transforms.*;
import view.Panel;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Controller3D {
    private final Panel panel;
    private final Renderer renderer;
    private LineRasterizer lineRasterizer;
    private final TriangleRasterizerTest triangleRasterizerTest;
    // Solids
    private List<Solid> solids = new ArrayList<>();
    private Solid axisX, axisY, axisZ;
    private int activeIndex = 0;

    // Camera
    private Camera camera;
    private Mat4 proj;

    // Proměnné pro ovládání myší
    private int oldX, oldY;
    private float mouseSensitivity = 0.005f;

    private boolean usePerspective = true;

    private ZBuffer zBuffer;
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

        // cubic pointy
        Point3D pA = new Point3D(-1, -1, 0.5);
        Point3D pB = new Point3D(-1, 2, 0.5);
        Point3D pC = new Point3D(1, 2, 0.5);
        Point3D pD = new Point3D(1, -1, 0.5);

        // 1. BEZIER
        Solid bezier = new SmartCubic(Cubic.BEZIER, pA, pB, pC, pD);
        // 2. COONS
        Solid coons = new SmartCubic(Cubic.COONS, pA, pB, pC, pD);
        // 3. FERGUSON
        Solid ferguson = new SmartCubic(Cubic.FERGUSON, pA, pD, pB, pC);
        Solid pyramid = new Pyramid();
        Solid cylinder = new Cylinder();
        Solid spiral = new Spiral();

        // Přidání do seznamu a pozice modelu
        solids.add(pyramid);
        pyramid.setModel(new Mat4Transl(-2, 0, 0));

        solids.add(cylinder);
        cylinder.setModel(new Mat4Transl(0, 2, 0));

        solids.add(spiral);
        spiral.setModel(new Mat4Transl(2, 0, 0));

        solids.add(ferguson);
        ferguson.setModel(new Mat4Transl(0, 0, 0));
        ferguson.setModel(new Mat4RotX(Math.toRadians(90)));

        solids.add(bezier);
        bezier.setModel(new Mat4Transl(0, 0 , 0));
        bezier.setModel(new Mat4RotX(Math.toRadians(90)).mul(new Mat4Transl(0, 0, 1)));

        solids.add(coons);
        coons.setModel(new Mat4Transl(0, 0, 0));
        coons.setModel(new Mat4RotX(Math.toRadians(90)).mul(new Mat4Transl(0, 0, -1)));

        axisX = new AxisX();
        axisX.setColor(new Col(1.0, 0.0, 0.0));
        axisX.setModel(new Mat4Identity());

        axisY = new AxisY();
        axisY.setColor(new Col(0.0, 1.0, 0.0));
        axisY.setModel(new Mat4Identity());

        axisZ = new AxisZ();
        axisZ.setColor(new Col(0.0, 0.0, 1.0));
        axisZ.setModel(new Mat4Identity());

        this.zBuffer = new ZBuffer(panel.getRaster());
        this.triangleRasterizerTest = new TriangleRasterizerTest(zBuffer);
        
        initListeners();
        drawScene();
    }


    private void initListeners() {
        panel.setFocusTraversalKeysEnabled(false);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // startovní pozice myši
                oldX = e.getX();
                oldY = e.getY();

                panel.requestFocusInWindow();
                drawScene();
            }
        });

        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                double dx = (oldX - e.getX()) * mouseSensitivity;
                double dy = (oldY - e.getY()) * mouseSensitivity;

                // otočení kameyr
                camera = camera.addAzimuth(dx);
                camera = camera.addZenith(dy);

                oldX = e.getX();
                oldY = e.getY();

                drawScene();
            }
        });

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                //Ovládání Kamery (WASD)
                if (e.getKeyCode() == KeyEvent.VK_W) camera = camera.forward(0.2);
                if (e.getKeyCode() == KeyEvent.VK_S) camera = camera.backward(0.2);
                if (e.getKeyCode() == KeyEvent.VK_A) camera = camera.left(0.2);
                if (e.getKeyCode() == KeyEvent.VK_D) camera = camera.right(0.2);

                //Výběr aktivního tělesa (tab)
                if (e.getKeyCode() == KeyEvent.VK_TAB) {
                    activeIndex++;
                    if (activeIndex >= solids.size()) {
                        activeIndex = 0;
                    }
                    System.out.println("Aktivní těleso index: " + activeIndex);
                }

                // změna projekce
                if (e.getKeyCode() == KeyEvent.VK_P) {
                    usePerspective = !usePerspective;
                    updateProjection();
                    drawScene();

                    System.out.println("Projekce: " + (usePerspective ? "Perspektivní" : "Pravoúhlá"));
                }

                //aktuální těleso
                Solid activeSolid = solids.get(activeIndex);
                Mat4 model = activeSolid.getModel();
                // test
                //translace (šipky)
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    activeSolid.setModel(model.mul(new Mat4Transl(0, 0.2, 0)));
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    activeSolid.setModel(model.mul(new Mat4Transl(0, -0.2, 0)));
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    activeSolid.setModel(model.mul(new Mat4Transl(-0.2, 0, 0)));
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    activeSolid.setModel(model.mul(new Mat4Transl(0.2, 0, 0)));
                }

                // rotace (x, y, z)
                if (e.getKeyCode() == KeyEvent.VK_X) {
                    activeSolid.setModel(model.mul(new Mat4RotX(Math.toRadians(10))));
                }
                if (e.getKeyCode() == KeyEvent.VK_Y) {
                    activeSolid.setModel(model.mul(new Mat4RotY(Math.toRadians(10))));
                }
                if (e.getKeyCode() == KeyEvent.VK_Z) {
                    activeSolid.setModel(model.mul(new Mat4RotZ(Math.toRadians(10))));
                }

                // scale (+ a -)
                if (e.getKeyCode() == KeyEvent.VK_ADD || e.getKeyCode() == KeyEvent.VK_PLUS) {
                    activeSolid.setModel(model.mul(new Mat4Scale(1.1)));
                }
                if (e.getKeyCode() == KeyEvent.VK_SUBTRACT || e.getKeyCode() == KeyEvent.VK_MINUS) {
                    activeSolid.setModel(model.mul(new Mat4Scale(0.9)));
                }

                drawScene();

            }
        });
    }


    private void drawScene() {
        // clear rasteru
        panel.getRaster().clear();
        zBuffer.getDepthBuffer().clear();

        renderer.setView(camera.getViewMatrix());
        renderer.setProj(proj);

        // renderovaní os
        renderer.render(axisX);
        renderer.render(axisY);
        renderer.render(axisZ);

        //renderovaní všech těles
//        for (int i = 0; i < solids.size(); i++) {
//            Solid solid = solids.get(i);
//            if (i == activeIndex) {
//                solid.setColor(new Col(0.0, 1.0, 0.0));
//            } else {
//                solid.setColor(new Col(1.0, 1.0, 1.0));
//            }
//            renderer.render(solid);
//        }

        triangleRasterizerTest.rasterize(new Vertex(400,0,0.5), new Vertex(0,300,0.5), new Vertex(799,599,0.5));
        panel.repaint();
    }


    private void updateProjection() {
        // aspect ratio
        double aspect = panel.getRaster().getHeight() / (double)panel.getRaster().getWidth();

        // přepínání projekce
        if (usePerspective) {
            proj = new Mat4PerspRH(Math.toRadians(90), aspect, 0.1, 100);
        } else {

            double height = 15.0;
            double width = height * aspect;
            proj = new Mat4OrthoRH(width, height, 0.1, 100);
        }

        zBuffer.setPixelWithZTest(50, 50, 0.1, new Col(255, 255, 255, 255));
        zBuffer.setPixelWithZTest(50, 50, 0.5, new Col(240, 50, 50, 255));
        renderer.setProj(proj);
    }


}


