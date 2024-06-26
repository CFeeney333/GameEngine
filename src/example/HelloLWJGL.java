package example;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class HelloLWJGL {

    private GLFWErrorCallback errorCallback;
    private GLFWKeyCallback keyCallback;

    private long window;
    private long window2;

    private float sp = 0.0f;
    private boolean swapcolor = false;

    private void init() {
        glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));

        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);

        int WIDTH = 300;
        int HEIGHT = 300;

        window = glfwCreateWindow(WIDTH, HEIGHT, "Hello LWJGL3", NULL, NULL);
        window2 = glfwCreateWindow(WIDTH, HEIGHT, "Another Title", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }
        if (window2 == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                    glfwSetWindowShouldClose(window, true);

                }
                if (key == GLFW_KEY_SPACE && action == GLFW_RELEASE) {
                    glfwMakeContextCurrent(window2);
                }
            }
        });
        glfwSetKeyCallback(window2, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window2, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                    glfwSetWindowShouldClose(window2, true);

                }
                if (key == GLFW_KEY_SPACE && action == GLFW_RELEASE) {
                    glfwMakeContextCurrent(window);
                }
            }
        });

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (vidmode.width() - WIDTH) / 2, (vidmode.height() - HEIGHT));
        glfwSetWindowPos(window2, (vidmode.width() - WIDTH) / 2, (vidmode.height() - HEIGHT));

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
        glfwShowWindow(window2);

    }

    private void update() {
        sp = sp + 0.001f;
        if (sp > 1.0f) {
            sp = 0.0f;
            swapcolor = !swapcolor;
        }
    }

    private void render() {
        drawQuad();
    }

    public void drawQuad() {
        if (!swapcolor) {
            glColor3f(0.0f, 1.0f, 0.0f);
        } else {
            glColor3f(0.0f, 0.0f, 1.0f);
        }

        glBegin(GL_QUADS);
        {
            glVertex3f(-sp, -sp, 0.0f);
            glVertex3f(sp, -sp, 0.0f);
            glVertex3f(sp, sp, 0.0f);
            glVertex3f(-sp, sp, 0.0f);
        }
        glEnd();
    }

    private void loop() {
        GL.createCapabilities();
        System.out.println("---------------------------");
        System.out.println("OpenGL Version : " + glGetString(GL_VERSION));
        System.out.println("OpenGL Max Texture Size : " + glGetInteger(GL_MAX_TEXTURE_SIZE));
        System.out.println("OpenGL Vendor : " + glGetString(GL_VENDOR));
        System.out.println("OpenGL Renderer : " + glGetString(GL_RENDERER));
        System.out.println("OpenGL Extensions supported by your card : ");
        String extensions = glGetString(GL_EXTENSIONS);
        String[] extArr = extensions.split("\\ ");
        for (String s : extArr) {
            System.out.println(s);
        }
        System.out.println("---------------------------");

        long currentWindow = glfwGetCurrentContext();
        while (!glfwWindowShouldClose(currentWindow)) {
            if (!swapcolor) {
                glClearColor(0.0f, 0.0f, 1.0f, 0.0f);

            } else {
                glClearColor(0.0f, 1.0f, 0.0f, 0.0f);
            }

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            update();
            render();
            glfwSwapBuffers(currentWindow);

            glfwPollEvents();
            currentWindow = glfwGetCurrentContext();
        }
    }

    public void run() {
        System.out.println("Hello LWJGL3 " + Version.getVersion() + "!");

        try {
            init();
            loop();
            glfwDestroyWindow(window);
            glfwDestroyWindow(window2);
            keyCallback.free();
        } finally {
            glfwTerminate();
            errorCallback.free();
        }
    }

    public static void main(String[] args) {
        new HelloLWJGL().run();
    }
}
