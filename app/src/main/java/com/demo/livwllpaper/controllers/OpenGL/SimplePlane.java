package com.demo.livwllpaper.controllers.OpenGL;


public class SimplePlane extends Mesh {
    public SimplePlane() {
        this(1.0f, 1.0f);
    }

    public SimplePlane(float f, float f2) {
        setIndices(new short[]{0, 1, 2, 1, 3, 2});
        setVertices(new float[]{-0.5f, -0.5f, 0.0f, 0.5f, -0.5f, 0.0f, -0.5f, 0.5f, 0.0f, 0.5f, 0.5f, 0.0f});
        setTextureCoordinates(new float[]{0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f});
    }
}
