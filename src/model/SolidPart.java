package model;

public class SolidPart {

    private final Topology topology;
    private final int primitiveCount;
    private final int start;

    public SolidPart(final Topology topology, final int primitiveCount, final int start) {
        this.topology = topology;
        this.primitiveCount = primitiveCount;
        this.start = start;
    }
}
