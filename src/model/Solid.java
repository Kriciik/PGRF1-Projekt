package model;

import transforms.Mat4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Solid {


    protected final List<Vertex> vertexBuffer;
    protected final List<Integer> indexBuffer;
    protected final List<SolidPart> partBuffer;
    private final Mat4 modelMat;

    public Solid(final List<Vertex> vertexBuffer, final List<Integer> indexBuffer,
                 final List<SolidPart> partBuffer, final Mat4 modelMat) {
        this.vertexBuffer = vertexBuffer;
        this.indexBuffer = indexBuffer;
        this.partBuffer = partBuffer;
        this.modelMat = modelMat;
    }
    public List<Vertex> getVertexBuffer() {
        return vertexBuffer;
    }

    public List<Integer> getIndexBuffer() {
        return indexBuffer;
    }

    public List<SolidPart> getPartBuffer() {
        return partBuffer;
    }

    public void addIndices (Integer... indices){
        indexBuffer.addAll(Arrays.asList(indices));
    }
}
