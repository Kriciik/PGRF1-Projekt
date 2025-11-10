package solids;

import model.Point;
import transforms.Point3D;

import java.util.ArrayList;
import java.util.List;

public abstract class Solid {
    protected List<Point3D> vertexBuffer = new ArrayList<Point3D>();
    protected List<Integer> indexBuffer = new ArrayList<Integer>();

    public List<Integer> getIndexBuffer() {
        return indexBuffer;
    }

    public List<Point3D> getVertexBuffer() {
        return vertexBuffer;
    }
}
