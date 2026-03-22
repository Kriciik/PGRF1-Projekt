package controller;

import model.*;
import raster.RasterBufferedImage;
import raster.TriangleRasterizerZBuffer;
import raster.ZBuffer;
import rasterize.LineRasterizer;
import rasterize.LineRasterizerTrivial;
import renderer.Renderer;
import renderer.RendererSolid;
import renderer.RendererWireframe;
import shader.Shader;
import shader.ShaderConstant;
import transforms.*;
import view.Panel;

import javax.imageio.ImageIO;
import java.awt.event.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Controller3D {
    private final Panel panel;

    private LineRasterizer lineRasterizer;
    private final TriangleRasterizerZBuffer triangleRasterizerZBuffer;

    private final RendererWireframe rendererWireframe;
    private final RendererSolid rendererSolid;
    private Renderer currentRenderer;

    // Solids
    private Solid sphere;
    private Solid cylinder;
    private Solid cube;
    private List<Solid> solids = new ArrayList<>();
    private Solid axes;
    private int activeIndex = 0;

    // Light
    private Solid lightBulb;
    private boolean useLighting = false;
    private Col ambientLightColor = new Col(40, 40, 40);
    private Col diffuseLightColor = new Col(255, 255, 0);

    // Camera
    private Camera camera;
    private Mat4 proj;

    // Proměnné pro ovládání myší
    private int oldX, oldY;
    private float mouseSensitivity = 0.005f;

    // perspektiva proměnná
    private boolean usePerspective = true;

    private ZBuffer zBuffer;


    // textury
    private boolean useTexture = false;

    public Controller3D(Panel panel) {
        this.panel = panel;
        this.lineRasterizer = new LineRasterizerTrivial(panel.getRaster());
        this.zBuffer = new ZBuffer(panel.getRaster());
        this.triangleRasterizerZBuffer = new TriangleRasterizerZBuffer(zBuffer);
        this.rendererSolid = new RendererSolid(
                lineRasterizer,
                triangleRasterizerZBuffer,
                panel.getRaster().getWidth(),
                panel.getRaster().getHeight()
                );
        this.rendererWireframe = new RendererWireframe(
                lineRasterizer,
                panel.getRaster().getWidth(),
                panel.getRaster().getHeight()
        );
        camera = new Camera()
                .withPosition(new Vec3D(0.4, -1.5, 1))
                .withAzimuth(Math.toRadians(90)) // levá - pravá
                .withZenith(Math.toRadians(-25)) // nahoru - dolů
                .withFirstPerson(true);
        proj = new Mat4PerspRH(Math.toRadians(90),
                panel.getRaster().getHeight() / (double)panel.getRaster().getWidth(),
                0.1,
                100);

        sphere = new Sphere(new Point3D(0, 0, -3), 1.0, 16, 16, new Col(0xffffff));
        cube = new Cube();
        cylinder = new Cylinder(0.6, 2.0, 20, new Col(0xff00ff));
        lightBulb = new Sphere(new Point3D(0, 0, 0), 0.2, 10, 10, diffuseLightColor);
        axes = new Axes();

        cube.setModel(new Mat4Transl(-3, 0, 0));
        cylinder.setModel(new Mat4Transl(3, 0, 0));


        lightBulb.setModel(new Mat4Transl(0, 3, 3));

        solids.clear();
        solids.add(sphere);
        solids.add(cube);
        solids.add(cylinder);

        solids.add(lightBulb);
        // Textury
        try {
            cylinder.setTexture(new RasterBufferedImage(ImageIO.read(new File("./res/textures/dio.png"))));
            cube.setTexture(new RasterBufferedImage(ImageIO.read(new File("./res/textures/gyro.png"))));
            sphere.setTexture(new RasterBufferedImage(ImageIO.read(new File("./res/textures/jotaro.png"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.currentRenderer = this.rendererSolid;

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

                if(e.getKeyCode() == KeyEvent.VK_T){
                    activeSolid = solids.get(activeIndex);
                    activeSolid.setShowTexture(!activeSolid.isShowTexture());
                    System.out.println("Textura " + (activeSolid.isShowTexture() ? "ZAPNUTA" : "VYPNUTA"));
                    drawScene();
                }

                if (e.getKeyCode() == KeyEvent.VK_M) {
                    if (currentRenderer == rendererSolid) {
                        currentRenderer = rendererWireframe;
                        System.out.println("Mód renderování: WIREFRAME");
                    } else {
                        currentRenderer = rendererSolid;
                        System.out.println("Mód renderování: SOLID");
                    }
                }

                if(e.getKeyCode() == KeyEvent.VK_L){
                  useLighting = !useLighting;
                    System.out.println("Světlo je: " + (useLighting ? "Zapnuto" : "Vypnuto"));
                }

                drawScene();
            }
        });
    }


    private void drawScene() {
        // clear rasteru
        panel.getRaster().clear();
        zBuffer.clear(new Col(0,0,0));

        rendererSolid.setView(camera.getViewMatrix());
        rendererSolid.setProj(proj);

        rendererWireframe.setView(camera.getViewMatrix());
        rendererWireframe.setProj(proj);

        Point3D currentLightPos = new Point3D(0, 0, 0).mul(lightBulb.getModel());

        for (int i = 0; i < solids.size(); i++) {
            Solid solid = solids.get(i);
            boolean isActive = (i == activeIndex);

            if (solid == lightBulb) {
                if (isActive) {
                    solid.setShader(new ShaderConstant(new Col(0, 255, 0)));
                } else {
                    solid.setShader(new ShaderConstant(diffuseLightColor));
                }
                currentRenderer.render(solid);
                continue;
            }

            solid.setShader(new Shader() {
                @Override
                public Col getColor(Vertex pixel) {

                    Col baseColor;
                    boolean hasTexture = solid.isShowTexture() && solid.getTexture() != null;

                    if (hasTexture) {
                        // Vykreslení textury
                        int x = (int) Math.round(pixel.getUv().getX() * (solid.getTexture().getWidth() - 1));
                        int y = (int) Math.round(pixel.getUv().getY() * (solid.getTexture().getHeight() - 1));
                        baseColor = solid.getTexture().getValue(x, y).orElse(new Col(0, 0, 0));

                        if (isActive) {
                            baseColor = baseColor.add(new Col(0, 80, 0)).saturate();
                        }
                    } else {
                        if (isActive) {
                            baseColor = new Col(0, 255, 0);
                        } else {
                            baseColor = pixel.getColor();
                        }
                    }
                    if (useLighting) {
                        Point3D pixelPos = pixel.getPositionWorldSpace();
                        Vec3D normal = pixel.getNormal().normalized().orElse(new Vec3D(0, 1, 0));

                        Vec3D lightDir = new Vec3D(
                                currentLightPos.getX() - pixelPos.getX(),
                                currentLightPos.getY() - pixelPos.getY(),
                                currentLightPos.getZ() - pixelPos.getZ()
                        ).normalized().orElse(new Vec3D(0, 1, 0));

                        double nDotL = Math.max(0, normal.dot(lightDir));

                        Col diffusePart = diffuseLightColor.mul(nDotL);
                        Col finalLight = ambientLightColor.add(diffusePart).saturate();

                        return baseColor.mul(finalLight).saturate();
                    }
                    return baseColor;
                }
            });
            currentRenderer.render(solid);
        }

        axes.setShader(new Shader() {
            @Override
            public Col getColor(Vertex pixel) {
                return pixel.getColor();
            }
        });
        currentRenderer.render(axes);

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

        rendererSolid.setProj(proj);
        rendererWireframe.setProj(proj);
    }


}


