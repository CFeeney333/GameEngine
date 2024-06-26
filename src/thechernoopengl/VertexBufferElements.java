package thechernoopengl;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;

public class VertexBufferElements {

    private int type;
    private boolean normalized;
    private int count;

    public int getType() {
        return this.type;
    }

    public boolean isNormalized() {
        return this.normalized;
    }

    public int getCount() {
        return this.count;
    }

    public VertexBufferElements(int type, boolean normalized, int count) {
        this.type = type;
        this.normalized = normalized;
        this.count = count;
    }

    static int getSizeOfType(int type) {
        int result;
        switch (type) {
            case GL_FLOAT:
                result = 4;
                break;
            case GL_UNSIGNED_INT:
                result = 4;
                break;
            default:
                result = 4;
                break;
        }
        return result;
    }
}
