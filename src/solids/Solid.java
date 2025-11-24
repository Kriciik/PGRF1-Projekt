package solids;

import model.Point;
import transforms.Point3D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Solid {
    protected List<Point3D> vertexBuffer = new ArrayList<Point3D>();
    protected List<Integer> indexBuffer = new ArrayList<Integer>();

    // TODO: vytvořit nový solid souřadnic x,y osy (ig) ze středu (jako novou třídu)

    public List<Integer> getIndexBuffer() {
        return indexBuffer;
    }

    public List<Point3D> getVertexBuffer() {
        return vertexBuffer;
    }

    public void addIndices( Integer... indices ){

        indexBuffer.addAll(Arrays.asList(indices));
    }
}
