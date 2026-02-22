package raster;

import transforms.Col;

import java.awt.*;
import java.util.Optional;

public class ZBuffer {
    private final Raster<Col> imageBuffer;
    private final Raster<Double> depthBuffer;

    public ZBuffer(Raster imageBuffer) {
        this.imageBuffer = imageBuffer;
        this.depthBuffer = new DepthBuffer(imageBuffer.getWidth(), imageBuffer.getHeight());
    }

    public void setPixelWithZTest(int x, int y, double z, Col color) {
        Optional<Double> value = depthBuffer.getValue(x,y);

        if(value.isEmpty() || z < value.get()) {

            depthBuffer.setValue(x, y, z);
            imageBuffer.setValue(x,y,color);
        }

    }

    public Raster<Double> getDepthBuffer() {
        return depthBuffer;
    }
}
