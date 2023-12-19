package com.demo.livwllpaper.wallpapermodule;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.view.Surface;

import com.demo.livwllpaper.R;
import com.google.android.exoplayer2.SimpleExoPlayer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Locale;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


class wallpaper20Renderer extends WallpaperRenderer {
    private static final int BYTES_PER_FLOAT = 4;
    private static final int BYTES_PER_INT = 4;
    private static final String TAG = "LPMLW20wallpaperRenderer";
    private final IntBuffer indices;
    private final FloatBuffer texCoords;
    private final FloatBuffer vertices;
    private int program = 0;
    private int mvpLocation = 0;
    private int positionLocation = 0;
    private int texCoordLocation = 0;
    private SurfaceTexture surfaceTexture = null;
    private int screenWidth = 0;
    private int screenHeight = 0;
    private int videoWidth = 0;
    private int videoHeight = 0;
    private int videoRotation = 0;
    private float xOffset = 0.0f;
    private float yOffset = 0.0f;
    private float maxXOffset = 0.0f;
    private float maxYOffset = 0.0f;
    private long updatedFrame = 0;
    private long renderedFrame = 0;
    private final int[] buffers = new int[3];
    private final int[] textures = new int[1];
    private final float[] mvp = {1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f};

    
    public wallpaper20Renderer(Context context) {
        super(context);
        FloatBuffer asFloatBuffer = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.vertices = asFloatBuffer;
        asFloatBuffer.put(new float[]{-1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, 1.0f}).position(0);
        FloatBuffer asFloatBuffer2 = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.texCoords = asFloatBuffer2;
        asFloatBuffer2.put(new float[]{0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f}).position(0);
        IntBuffer asIntBuffer = ByteBuffer.allocateDirect(24).order(ByteOrder.nativeOrder()).asIntBuffer();
        this.indices = asIntBuffer;
        asIntBuffer.put(new int[]{0, 1, 2, 3, 2, 1}).position(0);
    }

    @Override 
    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        GLES20.glDisable(2929);
        GLES20.glDepthMask(false);
        GLES20.glDisable(2884);
        GLES20.glDisable(3042);
        int[] iArr = this.textures;
        GLES20.glGenTextures(iArr.length, iArr, 0);
        GLES20.glBindTexture(36197, this.textures[0]);
        GLES20.glTexParameteri(36197, 10241, 9729);
        GLES20.glTexParameteri(36197, 10240, 9729);
        GLES20.glTexParameteri(36197, 10242, 33071);
        GLES20.glTexParameteri(36197, 10243, 33071);
        int linkProgramGLES20 = wallpaperUtils.linkProgramGLES20(wallpaperUtils.compileShaderResourceGLES20(this.context, 35633, R.raw.vertex_20), wallpaperUtils.compileShaderResourceGLES20(this.context, 35632, R.raw.fragment_20));
        this.program = linkProgramGLES20;
        this.mvpLocation = GLES20.glGetUniformLocation(linkProgramGLES20, "mvp");
        this.positionLocation = GLES20.glGetAttribLocation(this.program, "in_position");
        this.texCoordLocation = GLES20.glGetAttribLocation(this.program, "in_tex_coord");
        int[] iArr2 = this.buffers;
        GLES20.glGenBuffers(iArr2.length, iArr2, 0);
        GLES20.glBindBuffer(34962, this.buffers[0]);
        GLES20.glBufferData(34962, this.vertices.capacity() * 4, this.vertices, 35044);
        GLES20.glBindBuffer(34962, 0);
        GLES20.glBindBuffer(34962, this.buffers[1]);
        GLES20.glBufferData(34962, this.texCoords.capacity() * 4, this.texCoords, 35044);
        GLES20.glBindBuffer(34962, 0);
        GLES20.glBindBuffer(34963, this.buffers[2]);
        GLES20.glBufferData(34963, this.indices.capacity() * 4, this.indices, 35044);
        GLES20.glBindBuffer(34963, 0);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    @Override 
    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        GLES20.glViewport(0, 0, i, i2);
    }

    @Override 
    public void onDrawFrame(GL10 gl10) {
        SurfaceTexture surfaceTexture = this.surfaceTexture;
        if (surfaceTexture != null) {
            if (this.renderedFrame < this.updatedFrame) {
                surfaceTexture.updateTexImage();
                this.renderedFrame++;
            }
            GLES20.glClear(16384);
            GLES20.glUseProgram(this.program);
            GLES20.glUniformMatrix4fv(this.mvpLocation, 1, false, this.mvp, 0);
            GLES20.glBindBuffer(34962, this.buffers[0]);
            GLES20.glEnableVertexAttribArray(this.positionLocation);
            GLES20.glVertexAttribPointer(this.positionLocation, 2, 5126, false, 8, 0);
            GLES20.glBindBuffer(34962, this.buffers[1]);
            GLES20.glEnableVertexAttribArray(this.texCoordLocation);
            GLES20.glVertexAttribPointer(this.texCoordLocation, 2, 5126, false, 8, 0);
            GLES20.glBindBuffer(34963, this.buffers[2]);
            GLES20.glDrawElements(4, 6, 5125, 0);
            GLES20.glBindBuffer(34963, 0);
            GLES20.glDisableVertexAttribArray(this.texCoordLocation);
            GLES20.glDisableVertexAttribArray(this.positionLocation);
            GLES20.glBindBuffer(34962, 0);
            GLES20.glUseProgram(0);
        }
    }

    
    @Override 
    public void setSourcePlayer(SimpleExoPlayer simpleExoPlayer) {
        createSurfaceTexture();
        simpleExoPlayer.setVideoSurface(new Surface(this.surfaceTexture));
    }

    
    @Override 
    public void setScreenSize(int i, int i2) {
        if (this.screenWidth != i || this.screenHeight != i2) {
            this.screenWidth = i;
            this.screenHeight = i2;
            wallpaperUtils.debug(TAG, String.format(Locale.US, "Set screen size to %dx%d", Integer.valueOf(this.screenWidth), Integer.valueOf(this.screenHeight)));
            int i3 = this.screenWidth;
            int i4 = this.screenHeight;
            int i5 = this.videoWidth;
            int i6 = this.videoHeight;
            this.maxXOffset = (1.0f - ((((float) i3) / ((float) i4)) / (((float) i5) / ((float) i6)))) / 2.0f;
            this.maxYOffset = (1.0f - ((((float) i4) / ((float) i3)) / (((float) i6) / ((float) i5)))) / 2.0f;
            updateMatrix();
        }
    }

    
    @Override 
    public void setVideoSizeAndRotation(int i, int i2, int i3) {
        if (i3 % 180 != 0) {
            i2 = i;
            i = i2;
        }
        if (this.videoWidth != i || this.videoHeight != i2 || this.videoRotation != i3) {
            this.videoWidth = i;
            this.videoHeight = i2;
            this.videoRotation = i3;
            wallpaperUtils.debug(TAG, String.format(Locale.US, "Set video size to %dx%d", Integer.valueOf(this.videoWidth), Integer.valueOf(this.videoHeight)));
            wallpaperUtils.debug(TAG, String.format(Locale.US, "Set video rotation to %d", Integer.valueOf(this.videoRotation)));
            int i4 = this.screenWidth;
            int i5 = this.screenHeight;
            int i6 = this.videoWidth;
            int i7 = this.videoHeight;
            this.maxXOffset = (1.0f - ((((float) i4) / ((float) i5)) / (((float) i6) / ((float) i7)))) / 2.0f;
            this.maxYOffset = (1.0f - ((((float) i5) / ((float) i4)) / (((float) i7) / ((float) i6)))) / 2.0f;
            updateMatrix();
        }
    }

    
    @Override 
    public void setOffset(float f, float f2) {
        float f3 = this.maxXOffset;
        if (f > f3) {
            f = f3;
        }
        if (f < (-f3)) {
            f = -f3;
        }
        float f4 = this.maxYOffset;
        if (f2 > f4) {
            f2 = f4;
        }
        if (f2 < (-f3)) {
            f2 = -f4;
        }
        if (this.xOffset != f || this.yOffset != f2) {
            this.xOffset = f;
            this.yOffset = f2;
            wallpaperUtils.debug(TAG, String.format(Locale.US, "Set offset to %fx%f", Float.valueOf(this.xOffset), Float.valueOf(this.yOffset)));
            updateMatrix();
        }
    }

    private void createSurfaceTexture() {
        SurfaceTexture surfaceTexture = this.surfaceTexture;
        if (surfaceTexture != null) {
            surfaceTexture.release();
            this.surfaceTexture = null;
        }
        this.updatedFrame = 0;
        this.renderedFrame = 0;
        SurfaceTexture surfaceTexture2 = new SurfaceTexture(this.textures[0]);
        this.surfaceTexture = surfaceTexture2;
        surfaceTexture2.setDefaultBufferSize(this.videoWidth, this.videoHeight);
        this.surfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() { 
            @Override 
            public void onFrameAvailable(SurfaceTexture surfaceTexture3) {
                wallpaper20Renderer.this.updatedFrame++;
            }
        });
    }

    private void updateMatrix() {
        for (int i = 0; i < 16; i++) {
            this.mvp[i] = 0.0f;
        }
        float[] fArr = this.mvp;
        fArr[15] = 1.0f;
        fArr[10] = 1.0f;
        fArr[5] = 1.0f;
        fArr[0] = 1.0f;
        if (((float) this.videoWidth) / ((float) this.videoHeight) >= ((float) this.screenWidth) / ((float) this.screenHeight)) {
            wallpaperUtils.debug(TAG, "X-cropping");
            Matrix.scaleM(this.mvp, 0, (((float) this.videoWidth) / ((float) this.videoHeight)) / (((float) this.screenWidth) / ((float) this.screenHeight)), 1.0f, 1.0f);
            int i2 = this.videoRotation;
            if (i2 % 360 != 0) {
                Matrix.rotateM(this.mvp, 0, (float) (-i2), 0.0f, 0.0f, 1.0f);
            }
            Matrix.translateM(this.mvp, 0, this.xOffset, 0.0f, 0.0f);
            return;
        }
        wallpaperUtils.debug(TAG, "Y-cropping");
        Matrix.scaleM(this.mvp, 0, 1.0f, (((float) this.videoHeight) / ((float) this.videoWidth)) / (((float) this.screenHeight) / ((float) this.screenWidth)), 1.0f);
        int i3 = this.videoRotation;
        if (i3 % 360 != 0) {
            Matrix.rotateM(this.mvp, 0, (float) (-i3), 0.0f, 0.0f, 1.0f);
        }
        Matrix.translateM(this.mvp, 0, 0.0f, this.yOffset, 0.0f);
    }
}
