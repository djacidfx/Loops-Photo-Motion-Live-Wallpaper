package com.demo.livwllpaper.controllers.OpenGL;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class OpenGLRenderer implements GLSurfaceView.Renderer {
    private final Group root = new Group();

    @Override 
    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        gl10.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
        gl10.glShadeModel(7425);
        gl10.glClearDepthf(1.0f);
        gl10.glEnable(2929);
        gl10.glDepthFunc(515);
        gl10.glHint(3152, 4354);
    }

    @Override 
    public void onDrawFrame(GL10 gl10) {
        gl10.glClear(16640);
        gl10.glLoadIdentity();
        gl10.glTranslatef(0.0f, 0.0f, -4.0f);
        this.root.draw(gl10);
    }

    @Override 
    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        gl10.glViewport(0, 0, i, i2);
        gl10.glMatrixMode(5889);
        gl10.glLoadIdentity();
        GLU.gluPerspective(gl10, 45.0f, ((float) i) / ((float) i2), 0.1f, 1000.0f);
        gl10.glMatrixMode(5888);
        gl10.glLoadIdentity();
    }

    public void addMesh(Mesh lPMLWMesh) {
        this.root.add(lPMLWMesh);
    }
}
