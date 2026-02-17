package raster;

import model.Vertex;
import transforms.Point3D;

public abstract class TriangleRasterizer{
    //TODO: úkol na doma
    ZBuffer zBuffer;
    public abstract void rasterize(Vertex a,  Vertex b, Vertex c);
}
