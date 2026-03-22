package shader;

import model.Vertex;
import transforms.Col;

public class ShaderConstant implements Shader {
    private final Col color;

    public ShaderConstant(Col color) {
        this.color = color;
    }

    public ShaderConstant() {
        this.color = new Col(0xff0000);
    }

    @Override
    public Col getColor(Vertex pixel) {
        return color;
    }
}