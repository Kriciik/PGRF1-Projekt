package raster;

import java.nio.Buffer;
import java.util.Optional;

public class DepthBuffer implements Raster<Double> {
    private final double[][] buffer;
    private final int width;
    private final int height;

    public DepthBuffer( int width, int height) {
        this.width = width;
        this.height = height;
        this.buffer = new double[width][height];
    }

    @Override
    public void setValue(int x, int y, Double color) {
        // TODO: implementovat
        buffer[x][y] = color;
    }

    @Override
    public Optional<Double> getValue(int x, int y) {
        // TODO: implementovat
        return Optional.of(buffer[x][y]);
    }

    @Override
    public int getWidth() {
        // TODO: implementovat
        return 0;
    }

    @Override
    public int getHeight() {
        // TODO: implementovat
        return 0;
    }

    @Override
    public void clear() {
        // TODO: implementovat

    }
}
