package com.demo.livwllpaper.controllers.OpenGL;


public class FullFrameRect {
    private Texture2dProgram mProgram;
    private final Drawable2d mRectDrawable = new Drawable2d(Drawable2d.Prefab.FULL_RECTANGLE);

    public FullFrameRect(Texture2dProgram lPMLWTexture2dProgram) {
        this.mProgram = lPMLWTexture2dProgram;
    }

    public void release(boolean z) {
        Texture2dProgram lPMLWTexture2dProgram = this.mProgram;
        if (lPMLWTexture2dProgram != null) {
            if (z) {
                lPMLWTexture2dProgram.release();
            }
            this.mProgram = null;
        }
    }

    public Texture2dProgram getProgram() {
        return this.mProgram;
    }

    public void changeProgram(Texture2dProgram lPMLWTexture2dProgram) {
        this.mProgram.release();
        this.mProgram = lPMLWTexture2dProgram;
    }

    public int createTextureObject() {
        return this.mProgram.createTextureObject();
    }

    public void drawFrame(int i, float[] fArr) {
        this.mProgram.draw(GlUtil.IDENTITY_MATRIX, this.mRectDrawable.getVertexArray(), 0, this.mRectDrawable.getVertexCount(), this.mRectDrawable.getCoordsPerVertex(), this.mRectDrawable.getVertexStride(), fArr, this.mRectDrawable.getTexCoordArray(), i, this.mRectDrawable.getTexCoordStride());
    }
}
