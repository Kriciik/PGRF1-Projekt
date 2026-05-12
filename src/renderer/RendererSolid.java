package renderer;

import model.Solid;
import model.SolidPart;
import model.Vertex;
import raster.TriangleRasterizerZBuffer;
import rasterize.LineRasterizer;
import transforms.*;
import utils.Lerp;

public class RendererSolid implements Renderer {
    private LineRasterizer lineRasterizer;
    private TriangleRasterizerZBuffer triangleRasterizerZBuffer;

    private Mat4 view = new Mat4Identity();
    private Mat4 proj = new Mat4Identity();
    private int width;
    private int height;

    public RendererSolid(LineRasterizer lineRasterizer, TriangleRasterizerZBuffer triangleRasterizerZBuffer, int width, int height) {
        this.lineRasterizer = lineRasterizer;
        this.triangleRasterizerZBuffer = triangleRasterizerZBuffer;
        this.width = width;
        this.height = height;
    }

    public void render(Solid solid){

        Mat4 mvp = solid.getModel().mul(view).mul(proj);

        Lerp<Vertex> lerp = new Lerp<>();

        for(SolidPart part : solid.getPartBuffer()){
            switch(part.getType()){
                case POINTS:
                    // TODO: points
                    break;
                case LINES:
                    int index = part.getStartIndex();

                    for(int i = 0; i < part.getCount(); i++){
                        int indexA = solid.getIndexBuffer().get(index++);
                        int indexB = solid.getIndexBuffer().get(index++);

                        Vertex a = solid.getVertexBuffer().get(indexA);
                        Vertex b = solid.getVertexBuffer().get(indexB);

                        // Ořezání úsečky
                        Vertex aT = transformVertex(a, mvp, solid.getModel());
                        Vertex bT = transformVertex(b, mvp, solid.getModel());

                        if (aT != null && bT != null) {
                            Col lineColor = solid.getShader().getColor(a);

                            lineRasterizer.rasterize((int) Math.round(aT.getX()), (int) Math.round(aT.getY()), (int) Math.round(bT.getX()), (int) Math.round(bT.getY()), lineColor);


                        }
                    }
                    break;

                case TRIANGLES:
                    index = part.getStartIndex();

                    for(int i = 0; i < part.getCount(); i++){
                        int indexA = solid.getIndexBuffer().get(index++);
                        int indexB = solid.getIndexBuffer().get(index++);
                        int indexC = solid.getIndexBuffer().get(index++);

                        Vertex a = solid.getVertexBuffer().get(indexA);
                        Vertex b = solid.getVertexBuffer().get(indexB);
                        Vertex c = solid.getVertexBuffer().get(indexC);

                        Point3D pA = a.getPosition().mul(mvp);
                        Point3D pB = b.getPosition().mul(mvp);
                        Point3D pC = c.getPosition().mul(mvp);

                        if (pA.getZ() < 0 && pB.getZ() < 0 && pC.getZ() < 0) continue;

                        // Seřazení vrcholů podle Z
                        if (pA.getZ() > pB.getZ()) { Vertex tempV = a; a = b; b = tempV; Point3D tempP = pA; pA = pB; pB = tempP; }
                        if (pA.getZ() > pC.getZ()) { Vertex tempV = a; a = c; c = tempV; Point3D tempP = pA; pA = pC; pC = tempP; }
                        if (pB.getZ() > pC.getZ()) { Vertex tempV = b; b = c; c = tempV; Point3D tempP = pB; pB = pC; pC = tempP; }

                        double zMin = 0.0;

                        if (a.getZ() > b.getZ()) { Vertex temp = a; a = b; b = temp; }
                        if (a.getZ() > c.getZ()) { Vertex temp = a; a = c; c = temp; }
                        if (b.getZ() > c.getZ()) { Vertex temp = b; b = c; c = temp; }


                        //rozklad trojúhelníků
                        if (pA.getZ() < zMin) {
                            if (pB.getZ() < zMin) {
                                // A a B za kamerou
                                double tAC = (zMin - pA.getZ()) / (pC.getZ() - pA.getZ());
                                double tBC = (zMin - pB.getZ()) / (pC.getZ() - pB.getZ());

                                Vertex vAC = lerp.lerp(a, c, tAC);
                                Vertex vBC = lerp.lerp(b, c, tBC);

                                rasterizeTriangle(vAC, vBC, c, mvp, solid);
                            } else {
                                // A je za kamerou
                                double tAB = (zMin - pA.getZ()) / (pB.getZ() - pA.getZ());
                                double tAC = (zMin - pA.getZ()) / (pC.getZ() - pA.getZ());

                                Vertex vAB = lerp.lerp(a, b, tAB);
                                Vertex vAC = lerp.lerp(a, c, tAC);

                                rasterizeTriangle(vAB, b, c, mvp, solid);
                                rasterizeTriangle(vAB, c, vAC, mvp, solid);
                            }
                        } else {
                            // Vše před kamerou
                            rasterizeTriangle(a, b, c, mvp, solid);
                        }
                    }
                    break;
            }
        }
    }

    private void rasterizeTriangle(Vertex a, Vertex b, Vertex c, Mat4 mvp, Solid solid) {


        Vertex aT = transformVertex(a, mvp, solid.getModel());
        Vertex bT = transformVertex(b, mvp, solid.getModel());
        Vertex cT = transformVertex(c, mvp, solid.getModel());

        if (aT == null || bT == null || cT == null) return;

        triangleRasterizerZBuffer.rasterize(aT, bT, cT, solid.getShader());
    }

    private Vertex transformVertex(Vertex v, Mat4 mvp, Mat4 modelMatrix) {
        Point3D p = v.getPosition().mul(mvp);
        Point3D pWorld = v.getPosition().mul(modelMatrix);
        Vec3D normalWorld = v.getNormal().mul(modelMatrix.det());

        // if W <= 0, nevykreslím
        if (p.getW() <= 0) return null;
        // NDC
        double x = p.getX() / p.getW();
        double y = p.getY() / p.getW();
        double z = p.getZ() / p.getW();

        double winX = (x + 1) / 2.0 * (width - 1);
        double winY = (1 - y) / 2.0 * (height - 1);

        return new Vertex(
                new Point3D(winX, winY, z),
                pWorld,
                v.getColor(),
                v.getUv(),
                normalWorld
        );
    }
    @Override
    public void setView(Mat4 view) { this.view = view; }
    @Override
    public void setProj(Mat4 proj) { this.proj = proj; }

    public void setLineRasterizer(LineRasterizer lineRasterizer) {
        this.lineRasterizer = lineRasterizer;
    }
    public void setTriangleRasterizerTest(TriangleRasterizerZBuffer triangleRasterizerZBuffer) {
        this.triangleRasterizerZBuffer = triangleRasterizerZBuffer;
    }
}
