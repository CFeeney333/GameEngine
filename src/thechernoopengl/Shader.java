package thechernoopengl;

import thechernoopengl.maths.Mat4f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDeleteShader;

public class Shader {

    private int m_RendererID;
    private String m_Filepath;

    // hash map of String uniform name and integer location
    private HashMap<String, Integer> m_UniformLocationCache;

    public Shader(String filepath) {
        m_UniformLocationCache = new HashMap<>();
        m_Filepath = filepath;
        String[] shaders = parseShader(m_Filepath);
        m_RendererID = createShader(shaders[0], shaders[1]);
    }

    public void bind() {
        glUseProgram(m_RendererID);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void setUniform1i(String name, int value) {
        glUniform1i(getUniformLocation(name), value);
    }

    public void setUniform4f(String name, float v0, float v1, float v2, float v3) {
        glUniform4f(getUniformLocation(name), v0, v1, v2, v3);
    }

    public void setMatrix4f(String name, Mat4f projection) {
        glUniformMatrix4fv(getUniformLocation(name), false, projection.elements);
    }

    private int getUniformLocation(String name) {
        if (m_UniformLocationCache.containsKey(name)) {
            return m_UniformLocationCache.get(name);
        }

        int location;
        location = glGetUniformLocation(m_RendererID, name);
        if (location == -1) {
            System.out.println("Uniform " + name + " doesn't exist!");
        }
        m_UniformLocationCache.put(name, location);
        return location;
    }

    private int compileShader(int type, String source) {
        int id = glCreateShader(type);
        if (id == 0) {
            System.err.println("Error creating shader. Type: " + type);
        }

        glShaderSource(id, source);
        glCompileShader(id);

        int result = glGetShaderi(id, GL_COMPILE_STATUS);

        if (result == GL_FALSE) {
            String message = glGetShaderInfoLog(id);
            System.err.println("Failed to compile " +
                    (type==GL_VERTEX_SHADER ? "vertex" : "fragment") +
                    " shader!");
            System.err.println(message);
            glDeleteShader(id);
            return 0;
        }

        return id;
    }

    private String[] parseShader(String filepath) {
        StringBuilder vertexResult = new StringBuilder();
        StringBuilder fragmentResult = new StringBuilder();
        boolean isVertexShaderCode = false;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            String buffer;
            while ((buffer = reader.readLine()) != null) {
                if (buffer.contains("#shader vertex")) {
                    isVertexShaderCode = true;
                    continue;
                } else if (buffer.contains("#shader fragment")) {
                    isVertexShaderCode = false;
                    continue;
                }

                if (isVertexShaderCode) {
                    vertexResult.append(buffer).append('\n');
                } else {
                    fragmentResult.append(buffer).append('\n');
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String[]{vertexResult.toString(), fragmentResult.toString()};
    }

    private int createShader(String vertexShader, String fragmentShader) {
        int program = glCreateProgram();
        if (program == 0) {
            System.err.println("Could not create Shader");
        }
        int vs = compileShader(GL_VERTEX_SHADER, vertexShader);
        int fs = compileShader(GL_FRAGMENT_SHADER, fragmentShader);

        glAttachShader(program, vs);
        glAttachShader(program, fs);
        glLinkProgram(program);

        if (glGetProgrami(program, GL_LINK_STATUS) == 0) {
            System.err.println("Error linking Shader code: " + glGetProgramInfoLog(program, 1024));
        }
        glValidateProgram(program);
        if (glGetProgrami(program, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(program, 1024));
        }

        glDeleteShader(vs);
        glDeleteShader(fs);

        return program;
    }

    public void delete() {
        glDeleteProgram(m_RendererID);
    }

}
