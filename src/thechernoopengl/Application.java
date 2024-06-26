package thechernoopengl;

import org.lwjgl.opengl.GL;
import thechernoopengl.maths.Mat4f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Application {

    public static void main(String[] args) {
        long window;

        // Initialize the library
        if (!glfwInit()) {
            System.err.println("Unable to initialize GLFW");
        }

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        // Create a windowed mode window and its OpenGL context
        window = glfwCreateWindow(640, 480, "Hello World!", NULL, NULL);
        if (window == NULL) {
            glfwTerminate();
            System.err.println("Window is NULL");
        }

        // Make the window's context current
        glfwMakeContextCurrent(window);

        glfwSwapInterval(1);

        GL.createCapabilities();

        float[] vertices = new float[] {
                -0.5f, -0.5f, 0.0f, 0.0f,
                 0.5f, -0.5f, 1.0f, 0.0f,
                 0.5f,  0.5f, 1.0f, 1.0f,
                -0.5f,  0.5f, 0.0f, 1.0f
        };

        int[] indices = new int[]{
                0, 1, 2,
                2, 3, 0
        };

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        VertexArray va = new VertexArray();
        VertexBuffer vb = new VertexBuffer(vertices);

        VertexBufferLayout layout = new VertexBufferLayout();
        layout.pushFloats(2);
        layout.pushFloats(2);
        va.addBuffer(vb, layout);

        IndexBuffer ib = new IndexBuffer(indices);

        Mat4f proj = Mat4f.orthographic(-2.0f, 2.0f, -1.5f, 1.5f, -1.0f, 1.0f);

        Shader shader = new Shader("res/shaders/Basic.shader");
        shader.bind();
        shader.setUniform4f("u_Color", 0.8f, 0.3f, 0.8f, 1.0f);
        shader.setMatrix4f("u_MVP", proj);

        Texture texture = new Texture("res/textures/dumbLogo.png");
        texture.bind();
        shader.setUniform1i("u_Texture", 0); // 9 mins left (8:45)

        va.unbind();
        vb.unbind();
        ib.unbind();
        shader.unbind();

        Renderer renderer = new Renderer();

        float r = 0.0f;
        float increment = 0.05f;

        // Loop until the user closes the window
        while (!glfwWindowShouldClose(window)) {
            // Render here
            renderer.clear();

            shader.bind();
            shader.setUniform4f("u_Color", r, 0.3f, 0.8f, 1.0f);

            renderer.draw(va, ib, shader);

            if (r > 1.0f) {
                increment = -0.05f;
            } else if (r < 0.0f) {
                increment = 0.05f;
            }

            r += increment;

            // Swap front and back buffers
            glfwSwapBuffers(window);

            // Poll for and process events
            glfwPollEvents();

        }
        shader.delete();
        glfwTerminate();
    }
}
