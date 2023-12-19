package com.demo.livwllpaper.controllers.OpenGL;

import java.util.Vector;
import javax.microedition.khronos.opengles.GL10;


public class Group extends Mesh {
    private final Vector<Mesh> mChildren = new Vector<>();

    @Override 
    public void draw(GL10 gl10) {
        int size = this.mChildren.size();
        for (int i = 0; i < size; i++) {
            this.mChildren.get(i).draw(gl10);
        }
    }

    public void add(int i, Mesh lPMLWMesh) {
        this.mChildren.add(i, lPMLWMesh);
    }

    public boolean add(Mesh lPMLWMesh) {
        return this.mChildren.add(lPMLWMesh);
    }

    public void clear() {
        this.mChildren.clear();
    }

    public Mesh get(int i) {
        return this.mChildren.get(i);
    }

    public Mesh remove(int i) {
        return this.mChildren.remove(i);
    }

    public boolean remove(Object obj) {
        return this.mChildren.remove(obj);
    }

    public int size() {
        return this.mChildren.size();
    }
}
