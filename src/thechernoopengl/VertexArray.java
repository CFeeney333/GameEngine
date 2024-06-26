package thechernoopengl;

import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class VertexArray {

    private int m_RendererID;

    public VertexArray() {
        m_RendererID = glGenVertexArrays();
    }

    public void bind() {
        glBindVertexArray(m_RendererID);
    }

    public void unbind() {
        glBindVertexArray(0);
    }

    public void addBuffer(VertexBuffer vb, VertexBufferLayout layout) {
        this.bind();
        vb.bind();
        VertexBufferElements[] elements = layout.getElements();
        int offset = 0;
        for (int i = 0; i < elements.length; i++) {
            VertexBufferElements element = elements[i];
            glEnableVertexAttribArray(i);
            glVertexAttribPointer(i, element.getCount(), element.getType(), element.isNormalized(), layout.getStride(), offset);
            offset += element.getCount() * VertexBufferElements.getSizeOfType(element.getType());
        }
    }
}
