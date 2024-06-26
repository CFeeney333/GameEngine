package thechernoopengl;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class VertexBufferLayout {

    private ArrayList<VertexBufferElements> m_Elements = new ArrayList<>();
    private int m_Stride;

    public VertexBufferLayout() {
        m_Stride = 0;
    }

    public void pushFloats(int count) {
        m_Elements.add(new VertexBufferElements(GL_FLOAT, false, count));
        m_Stride += Float.BYTES * count;
    }

    public void pushUnsignedInts(int count) {
        m_Elements.add(new VertexBufferElements(GL_UNSIGNED_INT, false, count));
        m_Stride += Integer.BYTES * count;
    }

    public int getStride() {
        return m_Stride;
    }

    public VertexBufferElements[] getElements() {
        VertexBufferElements[] result = new VertexBufferElements[m_Elements.size()];
        for (int i = 0; i < m_Elements.size(); i++) {
            result[i] = m_Elements.get(i);
        }
        return result;
    }
}
