package com.cathal.flappy;

import com.cathal.flappy.graphics.Shader;
import com.cathal.flappy.input.Input;
import com.cathal.flappy.level.Level;
import com.cathal.flappy.maths.Matrix4f;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main implements Runnable{

    private final int WIDTH = 1280;
    private final int HEIGHT = 720;

    private Thread thread;
    private boolean running = false;

    private long window;

    private Level level;

    public void start() {
        running = true;
        thread = new Thread(this, "Game");
        thread.start();
    }
    public void run() {
        init();
        while (running) {
            update();
            render();

            if (glfwWindowShouldClose(window)) {
                running = false;
            }
        }
    }

    private void init() {
        if (!glfwInit()) {
            // TODO: handle it

        }
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        window = glfwCreateWindow(WIDTH, HEIGHT, "Flappy", NULL, NULL);
        if (window == NULL) {
            // TODO: handle
            return;
        }

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (vidmode.width() - WIDTH) / 2, (vidmode.height() - HEIGHT) / 2);

        glfwSetKeyCallback(window, new Input());

        glfwMakeContextCurrent(window);
        glfwShowWindow(window);
        GL.createCapabilities();

        glClearColor(1.0f, 0.0f, 1.0f, 1.0f);
        glEnable(GL_DEPTH_TEST);
        glActiveTexture(GL_TEXTURE1);
        System.out.println("OpenGL : " + glGetString(GL_VERSION));
        Shader.loadAll();

        Shader.BG.enable();
        Matrix4f pr_matrix = Matrix4f.orthographic(-10.0f, 10.0f, -(10.0f * 9.0f) / 16.0f, (10.0f * 9.0f) / 16.0f, -1.0f, 1.0f);
        Shader.BG.setUniformMat4f("pr_matrix", pr_matrix);
        Shader.BG.setUniform1i("tex", 1);
        Shader.BG.disable();

        level = new Level();
    }

    private void update() {
        glfwPollEvents();
        if (Input.keys[GLFW_KEY_SPACE]) {
            System.out.println("Flap!");

        }
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        level.render();

        int error = glGetError();
        if (error != GL_NO_ERROR) {
            System.out.println(error);
        }

        glfwSwapBuffers(window);
    }
    public static void main(String[] args) {
        new Main().start();
    }

}
