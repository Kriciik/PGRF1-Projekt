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
    public void setValue(int x, int y, Double z) {
        buffer[x][y] = z;
    }

    @Override
    public Optional<Double> getValue(int x, int y) {
        return Optional.of(buffer[x][y]);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void clear() {
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                buffer[i][j] = 1;
            }
        }
    }
}
