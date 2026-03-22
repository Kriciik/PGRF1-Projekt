package raster;

import model.Vertex;
import shader.Shader;

public abstract class TriangleRasterizer {
    protected final ZBuffer zBuffer;

    public TriangleRasterizer(ZBuffer zBuffer) {
        this.zBuffer = zBuffer;
    }

    public abstract void rasterize(Vertex a, Vertex b, Vertex c, Shader shader);
}