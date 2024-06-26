package homework;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main implements Runnable {

    private Thread thread;
    private boolean running;
    private long window;

    private void start() {
        // set running to false
        running = false;

        // initialize the thread
        thread = new Thread(this, "GameThread");

        // start the thread
        thread.start();
    }

    public static void main(String[] args) {
        new Main().start();
    }

    @Override
    public void run() {
        init();
        running = true;
        while (running) {
            if (glfwWindowShouldClose(window)) {
                running = false;
                break;
            }
            update();
            render();
        }
        glfwTerminate();
    }

    private void render() {
        glfwSwapBuffers(window);

    }

    private void update() {
        glfwPollEvents();
    }

    private void init() {
        if (!glfwInit()) {
            System.err.println("Failed to initialize glfw");
        }

        window = glfwCreateWindow(640, 480, "Hello World", NULL, NULL);
        if (window == NULL) {
            System.err.println("Failed to initialize window");
        }

        float[] vertices = new float[] {
                -0.5f, -0.5f,
                 0.0f, 0.5f,
                 0.5f, -0.5f
        };

        FloatBuffer buffer = MemoryUtil.memAllocFloat(vertices.length);
        buffer.put(vertices).flip();

        int vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
    }
}
