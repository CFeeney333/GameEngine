package thechernoopengl;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.stb.STBImage;

public class Texture {

    private int m_RendererID;
    private String m_Filepath;
    private ByteBuffer m_LocalBuffer;
    private int[] m_Width, m_Height, m_BPP;

    public Texture(String filepath) {
        m_Width = new int[1];
        m_Height = new int[1];
        m_BPP = new int[1];
        m_Filepath = filepath;
        m_RendererID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, m_RendererID);

        STBImage.stbi_set_flip_vertically_on_load(true);
        m_LocalBuffer = STBImage.stbi_load(m_Filepath, m_Width, m_Height, m_BPP, 4);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, getWidth(), getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, m_LocalBuffer);
        glBindTexture(GL_TEXTURE_2D, 0);

        if (m_LocalBuffer != null) {
            STBImage.stbi_image_free(m_LocalBuffer);
        }
    }

    public int getWidth() {
        return m_Width[0];
    }

    public int getHeight() {
        return m_Height[0];
    }

    public void bind(int slot) {
        glActiveTexture(GL_TEXTURE0 + slot);
        glBindTexture(GL_TEXTURE_2D, m_RendererID);
    }

    /**
     * Binds to slot 0
     */
    public void bind() {
        bind(0);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}
