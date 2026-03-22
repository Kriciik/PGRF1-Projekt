package raster;

import transforms.Col;

import java.awt.*;
import java.util.Optional;

public class ZBuffer {
    private final Raster<Col> imageBuffer;
    private final Raster<Double> depthBuffer;

    public ZBuffer(Raster<Col> imageBuffer) {
        this.imageBuffer = imageBuffer;
        this.depthBuffer = new DepthBuffer(imageBuffer.getWidth(), imageBuffer.getHeight());
    }

    public void setPixelWithZTest(int x, int y, double z, Col color) {

        if (x < 0 || x >= imageBuffer.getWidth() || y < 0 || y >= imageBuffer.getHeight()) {
            return;
        }

        double currentZ = depthBuffer.getValue(x, y).orElse(1.0);

        if (z < currentZ) {
            depthBuffer.setValue(x, y, z);
            imageBuffer.setValue(x, y, color);
        }

    }

    public Raster<Double> getDepthBuffer() {
        return depthBuffer;
    }

    public int getWidth() {
        return imageBuffer.getWidth();
    }

    public int getHeight() {
        return imageBuffer.getHeight();
    }

    public void clear(Col backgroundColor) {
        for (int y = 0; y < imageBuffer.getHeight(); y++) {
            for (int x = 0; x < imageBuffer.getWidth(); x++) {
                imageBuffer.setValue(x, y, backgroundColor);
            }
        }
        for (int y = 0; y < depthBuffer.getHeight(); y++) {
            for (int x = 0; x < depthBuffer.getWidth(); x++) {
                depthBuffer.setValue(x, y, 1.0);
            }
        }
    }

}
