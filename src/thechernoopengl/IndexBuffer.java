package thechernoopengl;

import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;

public class IndexBuffer {

    private int m_RendererID;
    private int m_Count;

    public IndexBuffer(int[] data) {
        m_Count = data.length;
        m_RendererID = glGenBuffers();
        IntBuffer buffer = MemoryUtil.memAllocInt(m_Count);
        buffer.put(data).flip();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m_RendererID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
    }

    public void bind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m_RendererID);
    }

    public void unbind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public void delete() {
        glDeleteBuffers(m_RendererID);
    }

    public int getCount() {
        return m_Count;
    }
}
