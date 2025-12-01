package solids;

import model.Point;
import transforms.Col;
import transforms.Mat4;
import transforms.Mat4Identity;
import transforms.Point3D;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Solid {
    protected List<Point3D> vertexBuffer = new ArrayList<Point3D>();
    protected List<Integer> indexBuffer = new ArrayList<Integer>();
    protected Mat4 model = new Mat4Identity();
    protected Col color = new Col(0xff0000);
    // TODO: vytvořit nový solid souřadnic x,y osy (ig) ze středu (jako novou třídu)

    public List<Integer> getIndexBuffer() {
        return indexBuffer;
    }

    public List<Point3D> getVertexBuffer() {
        return vertexBuffer;
    }

    public Mat4 getModel() {
        return model;
    }

    public void setModel(Mat4 model) {
        this.model = model;
    }

    public void addIndices(Integer... indices ){

        indexBuffer.addAll(Arrays.asList(indices));
    }
}
