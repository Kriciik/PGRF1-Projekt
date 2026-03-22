package renderer;

import model.Solid;
import model.SolidPart;
import model.Vertex;
import rasterize.LineRasterizer;
import transforms.Col;
import transforms.Mat4;
import transforms.Mat4Identity;
import transforms.Point3D;

public class RendererWireframe implements Renderer {
    private LineRasterizer lineRasterizer;

    private Mat4 view = new Mat4Identity();
    private Mat4 proj = new Mat4Identity();
    private int width;
    private int height;

    public RendererWireframe(LineRasterizer lineRasterizer, int width, int height) {
        this.lineRasterizer = lineRasterizer;
        this.width = width;
        this.height = height;
    }

    @Override
    public void render(Solid solid) {
        Mat4 mvp = solid.getModel().mul(view).mul(proj);

        Col color = solid.getShader().getColor(new Vertex(new Point3D(0,0,0), new Col(255,255,255)));

        for (SolidPart part : solid.getPartBuffer()) {
            int index = part.getStartIndex();

            switch (part.getType()) {
                case LINES:
                    for (int i = 0; i < part.getCount(); i++) {
                        int indexA = solid.getIndexBuffer().get(index++);
                        int indexB = solid.getIndexBuffer().get(index++);

                        Vertex a = solid.getVertexBuffer().get(indexA);
                        Vertex b = solid.getVertexBuffer().get(indexB);

                        drawLine(a, b, mvp, color);
                    }
                    break;

                case TRIANGLES:
                    for (int i = 0; i < part.getCount(); i++) {
                        int indexA = solid.getIndexBuffer().get(index++);
                        int indexB = solid.getIndexBuffer().get(index++);
                        int indexC = solid.getIndexBuffer().get(index++);

                        Vertex a = solid.getVertexBuffer().get(indexA);
                        Vertex b = solid.getVertexBuffer().get(indexB);
                        Vertex c = solid.getVertexBuffer().get(indexC);

                        drawLine(a, b, mvp, color);
                        drawLine(b, c, mvp, color);
                        drawLine(c, a, mvp, color);
                    }
                    break;
            }
        }
    }

    private void drawLine(Vertex a, Vertex b, Mat4 mvp, Col color) {
        Point3D pA = a.getPosition().mul(mvp);
        Point3D pB = b.getPosition().mul(mvp);

        if (pA.getW() <= 0 || pB.getW() <= 0) return;

        double x1 = pA.getX() / pA.getW();
        double y1 = pA.getY() / pA.getW();

        double x2 = pB.getX() / pB.getW();
        double y2 = pB.getY() / pB.getW();

        double winX1 = (x1 + 1) / 2.0 * (width - 1);
        double winY1 = (1 - y1) / 2.0 * (height - 1);

        double winX2 = (x2 + 1) / 2.0 * (width - 1);
        double winY2 = (1 - y2) / 2.0 * (height - 1);


        lineRasterizer.rasterize((int) Math.round(winX1), (int) Math.round(winY1), (int) Math.round(winX2), (int) Math.round(winY2), color);

    }

    @Override
    public void setView(Mat4 view) { this.view = view; }
    @Override
    public void setProj(Mat4 proj) { this.proj = proj; }
}