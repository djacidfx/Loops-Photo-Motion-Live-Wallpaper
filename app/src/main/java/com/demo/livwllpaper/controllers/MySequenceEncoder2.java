package com.demo.livwllpaper.controllers;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLExt;
import android.opengl.EGLSurface;
import android.opengl.GLES20;
import android.os.Build;
import android.view.Surface;

import androidx.annotation.RequiresApi;

import com.demo.livwllpaper.Utilsx.Utils;
import com.demo.livwllpaper.controllers.OpenGL.FullFrameRect;
import com.demo.livwllpaper.controllers.OpenGL.GlUtil;
import com.demo.livwllpaper.controllers.OpenGL.Texture2dProgram;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;


public class MySequenceEncoder2 {
    private static final int IFRAME_INTERVAL = -1;
    private static final String MIME_TYPE = "video/avc";
    private static final String TAG = "EncodeAndMuxTest";
    private static final boolean VERBOSE = false;
    private int fps;
    private int frameNo;
    private int mBitRate;
    private MediaCodec mEncoder;
    private int mHeight;
    private CodecInputSurface mInputSurface;
    private FullFrameRect mLPMLWFullFrameRect;
    private MediaMuxer mMuxer;
    private boolean mMuxerStarted;
    private int mTrackIndex;
    private int mWidth;
    private int mTextureId = -1;
    private MediaCodec.BufferInfo mBufferInfo = new MediaCodec.BufferInfo();

    
    
    public static class CodecInputSurface {
        private static final int EGL_RECORDABLE_ANDROID = 12610;
        private EGLContext mEGLContext = EGL14.EGL_NO_CONTEXT;
        private EGLDisplay mEGLDisplay = EGL14.EGL_NO_DISPLAY;
        private EGLSurface mEGLSurface = EGL14.EGL_NO_SURFACE;
        private Surface mSurface;

        public CodecInputSurface(Surface surface) {
            Objects.requireNonNull(surface);
            this.mSurface = surface;
            eglSetup();
        }

        private void eglSetup() {
            EGLDisplay eglGetDisplay = EGL14.eglGetDisplay(0);
            this.mEGLDisplay = eglGetDisplay;
            if (eglGetDisplay != EGL14.EGL_NO_DISPLAY) {
                int[] iArr = new int[2];
                if (EGL14.eglInitialize(this.mEGLDisplay, iArr, 0, iArr, 1)) {
                    EGLConfig[] eGLConfigArr = new EGLConfig[1];
                    EGL14.eglChooseConfig(this.mEGLDisplay, new int[]{12324, 8, 12323, 8, 12322, 8, 12321, 8, 12352, 4, EGL_RECORDABLE_ANDROID, 1, 12344}, 0, eGLConfigArr, 0, 1, new int[1], 0);
                    checkEglError("eglCreateContext RGB888+recordable ES2");
                    this.mEGLContext = EGL14.eglCreateContext(this.mEGLDisplay, eGLConfigArr[0], EGL14.EGL_NO_CONTEXT, new int[]{12440, 2, 12344}, 0);
                    checkEglError("eglCreateContext");
                    this.mEGLSurface = EGL14.eglCreateWindowSurface(this.mEGLDisplay, eGLConfigArr[0], this.mSurface, new int[]{12344}, 0);
                    checkEglError("eglCreateWindowSurface");
                    return;
                }
                throw new RuntimeException("unable to initialize EGL14");
            }
            throw new RuntimeException("unable to get EGL14 display");
        }

        public void release() {
            if (this.mEGLDisplay != EGL14.EGL_NO_DISPLAY) {
                EGL14.eglMakeCurrent(this.mEGLDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT);
                EGL14.eglDestroySurface(this.mEGLDisplay, this.mEGLSurface);
                EGL14.eglDestroyContext(this.mEGLDisplay, this.mEGLContext);
                EGL14.eglReleaseThread();
                EGL14.eglTerminate(this.mEGLDisplay);
            }
            this.mSurface.release();
            this.mEGLDisplay = EGL14.EGL_NO_DISPLAY;
            this.mEGLContext = EGL14.EGL_NO_CONTEXT;
            this.mEGLSurface = EGL14.EGL_NO_SURFACE;
            this.mSurface = null;
        }

        public void makeCurrent() {
            EGLDisplay eGLDisplay = this.mEGLDisplay;
            EGLSurface eGLSurface = this.mEGLSurface;
            EGL14.eglMakeCurrent(eGLDisplay, eGLSurface, eGLSurface, this.mEGLContext);
            checkEglError("eglMakeCurrent");
        }

        public boolean swapBuffers() {
            boolean eglSwapBuffers = EGL14.eglSwapBuffers(this.mEGLDisplay, this.mEGLSurface);
            checkEglError("eglSwapBuffers");
            return eglSwapBuffers;
        }

        public void setPresentationTime(long j) {
            EGLExt.eglPresentationTimeANDROID(this.mEGLDisplay, this.mEGLSurface, j);
            checkEglError("eglPresentationTimeANDROID");
        }

        private void checkEglError(String str) {
            int eglGetError = EGL14.eglGetError();
            if (eglGetError != 12288) {
                throw new RuntimeException(str + ": EGL error: 0x" + Integer.toHexString(eglGetError));
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MediaCodec configDecoder(MediaFormat mediaFormat, Surface surface) {
        if (!(mediaFormat == null || surface == null)) {
            String string = mediaFormat.getString("mime");
            MediaCodecInfo[] codecInfos = new MediaCodecList(0).getCodecInfos();
            for (MediaCodecInfo mediaCodecInfo : codecInfos) {
                try {
                    if (mediaCodecInfo.getCapabilitiesForType(string).isFormatSupported(mediaFormat)) {
                        MediaCodec createByCodecName = MediaCodec.createByCodecName(mediaCodecInfo.getName());
                        try {
                            createByCodecName.configure(mediaFormat, surface, (MediaCrypto) null, 0);
                            return createByCodecName;
                        } catch (IllegalArgumentException unused) {
                            createByCodecName.release();
                        } catch (IllegalStateException unused2) {
                            createByCodecName.release();
                        }
                    } else {
                        continue;
                    }
                } catch (IOException | IllegalArgumentException unused3) {
                }
            }
        }
        return null;
    }

    public MySequenceEncoder2(File file, int i, int i2, int i3, int i4) throws Exception {
        this.fps = 30;
        this.frameNo = 0;
        this.mBitRate = 4000000;
        this.mWidth = i;
        this.mHeight = i2;
        this.fps = i3;
        if (i >= Utils.width) {
            int i5 = Utils.width;
        } else if (i != 1080) {
            int i6 = Utils.width;
        }
        if (i2 >= Utils.height) {
            int i7 = Utils.height;
        } else if (i2 != 1080) {
            int i8 = Utils.height;
        }
        MediaFormat createVideoFormat = MediaFormat.createVideoFormat("video/avc", i, i2);
        Math.max(i, i2);
        this.mBitRate = 200000000;
        this.mBitRate = Math.round(((float) (i * i2 * i3)) * (((float) i4) / 1000.0f) * 0.175f);
        createVideoFormat.setInteger("color-format", 2130708361);
        createVideoFormat.setInteger("bitrate", this.mBitRate);
        createVideoFormat.setInteger("frame-rate", i3);
        createVideoFormat.setInteger("i-frame-interval", 1);
        try {
            MediaCodec createEncoderByType = MediaCodec.createEncoderByType("video/avc");
            this.mEncoder = createEncoderByType;
            createEncoderByType.configure(createVideoFormat, (Surface) null, (MediaCrypto) null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        } catch (Exception unused) {
        }
        this.mInputSurface = new CodecInputSurface(this.mEncoder.createInputSurface());
        this.mEncoder.start();
        try {
            this.mMuxer = new MediaMuxer(file.getPath(), MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            this.mTrackIndex = -1;
            this.mMuxerStarted = false;
            this.mInputSurface.makeCurrent();
            this.mLPMLWFullFrameRect = new FullFrameRect(new Texture2dProgram(Texture2dProgram.ProgramType.TEXTURE_2D));
            this.frameNo = 0;
        } catch (Exception e) {
            throw new RuntimeException("MediaMuxer creation failed", e);
        }
    }

    public void loadTexture(Bitmap bitmap) {
        int i = this.mTextureId;
        if (i == -1) {
            GLES20.glGenTextures(1, new int[1], 0);
            GLES20.glBindTexture(3553, this.mTextureId);
            GLES20.glTexParameterf(3553, 10241, 9729.0f);
            GLES20.glTexParameterf(3553, 10240, 9729.0f);
            GLES20.glTexParameteri(3553, 10242, 33071);
            GLES20.glTexParameteri(3553, 10243, 33071);
        } else {
            GLES20.glBindTexture(3553, i);
        }
        bitmap.getByteCount();
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(bitmap.getWidth() * bitmap.getHeight() * 4);
        allocateDirect.order(ByteOrder.BIG_ENDIAN);
        bitmap.copyPixelsToBuffer(allocateDirect);
        allocateDirect.position(0);
        GLES20.glTexImage2D(3553, 0, 6408, bitmap.getWidth(), bitmap.getHeight(), 0, 6408, 5121, allocateDirect);
    }

    public void encodeFrame(Bitmap bitmap) throws IOException {
        drainEncoder(false);
        Matrix matrix = new Matrix();
        matrix.postScale(1.0f, -1.0f);
        loadTexture(Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false));
        try {
            if (this.mWidth != 1080) {
                int i = Utils.width / 2;
                int i2 = this.mWidth / 2;
            }
            if (this.mHeight != 1080) {
                int i3 = Utils.height / 2;
                int i4 = this.mHeight / 2;
            }
            GLES20.glViewport(0, 0, this.mWidth, this.mHeight);
        } catch (Exception unused) {
            GLES20.glViewport(0, 0, this.mWidth, this.mHeight);
        }
        this.mLPMLWFullFrameRect.drawFrame(this.mTextureId, GlUtil.IDENTITY_MATRIX);
        this.mInputSurface.setPresentationTime(computePresentationTimeNsec(this.frameNo));
        this.mInputSurface.swapBuffers();
        this.frameNo++;
    }

    private void releaseEncoder() {
        MediaCodec mediaCodec = this.mEncoder;
        if (mediaCodec != null) {
            mediaCodec.stop();
            this.mEncoder.release();
            this.mEncoder = null;
        }
        CodecInputSurface codecInputSurface = this.mInputSurface;
        if (codecInputSurface != null) {
            codecInputSurface.release();
            this.mInputSurface = null;
        }
        MediaMuxer mediaMuxer = this.mMuxer;
        if (mediaMuxer != null) {
            mediaMuxer.stop();
            this.mMuxer.release();
            this.mMuxer = null;
        }
    }

    private void drainEncoder(boolean z) {
        if (z) {
            this.mEncoder.signalEndOfInputStream();
        }
        ByteBuffer[] outputBuffers = this.mEncoder.getOutputBuffers();
        while (true) {
            int dequeueOutputBuffer = this.mEncoder.dequeueOutputBuffer(this.mBufferInfo, 10000);
            if (dequeueOutputBuffer == -1) {
                if (!z) {
                    return;
                }
            } else if (dequeueOutputBuffer == -3) {
                outputBuffers = this.mEncoder.getOutputBuffers();
            } else if (dequeueOutputBuffer == -2) {
                if (!this.mMuxerStarted) {
                    this.mTrackIndex = this.mMuxer.addTrack(this.mEncoder.getOutputFormat());
                    this.mMuxer.start();
                    this.mMuxerStarted = true;
                } else {
                    throw new RuntimeException("format changed twice");
                }
            } else if (dequeueOutputBuffer >= 0) {
                ByteBuffer byteBuffer = outputBuffers[dequeueOutputBuffer];
                if (byteBuffer != null) {
                    if ((this.mBufferInfo.flags & 2) != 0) {
                        this.mBufferInfo.size = 0;
                    }
                    if (this.mBufferInfo.size != 0) {
                        if (this.mMuxerStarted) {
                            byteBuffer.position(this.mBufferInfo.offset);
                            byteBuffer.limit(this.mBufferInfo.offset + this.mBufferInfo.size);
                            this.mMuxer.writeSampleData(this.mTrackIndex, byteBuffer, this.mBufferInfo);
                        } else {
                            throw new RuntimeException("muxer hasn't started");
                        }
                    }
                    this.mEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                    if ((this.mBufferInfo.flags & 4) != 0) {
                        return;
                    }
                } else {
                    throw new RuntimeException("encoderOutputBuffer " + dequeueOutputBuffer + " was null");
                }
            } else {
                continue;
            }
        }
    }

    private long computePresentationTimeNsec(int i) {
        return (((long) i) * 1000000000) / ((long) this.fps);
    }

    public boolean finish() throws IOException {
        drainEncoder(true);
        releaseEncoder();
        this.frameNo = 0;
        return true;
    }
}
