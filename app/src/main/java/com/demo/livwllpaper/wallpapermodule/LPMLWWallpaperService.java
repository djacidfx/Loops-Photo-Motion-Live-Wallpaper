package com.demo.livwllpaper.wallpapermodule;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;

import com.demo.livwllpaper.R;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.List;


public class LPMLWWallpaperService extends WallpaperService {
    private static final String TAG = "LPMLWWallpaperService";

    
    public class GLWallpaperEngine extends Engine {
        private static final String TAG = "GLWallpaperEngine";
        private final Context context;
        private GLWallpaperSurfaceView glSurfaceView = null;
        private SimpleExoPlayer exoPlayer = null;
        private MediaSource videoSource = null;
        private DefaultTrackSelector trackSelector = null;
        private WallpaperCardModel WallpaperCardModel = null;
        private WallpaperCardModel oldWallpaperCardModel = null;
        private WallpaperRenderer renderer = null;
        private boolean allowSlide = false;
        private int videoRotation = 0;
        private int videoWidth = 0;
        private int videoHeight = 0;
        private long progress = 0;

        
        
        public class GLWallpaperSurfaceView extends GLSurfaceView {
            private static final String TAG = "GLWallpaperSurface";

            public GLWallpaperSurfaceView(Context context) {
                super(context);
            }

            @Override 
            public SurfaceHolder getHolder() {
                return GLWallpaperEngine.this.getSurfaceHolder();
            }

            void onDestroy() {
                super.onDetachedFromWindow();
            }
        }

        GLWallpaperEngine(Context context) {
            this.context = context;
            setTouchEventsEnabled(false);
        }

        @Override 
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            this.allowSlide = LPMLWWallpaperService.this.getSharedPreferences(LPMLWApplication.OPTIONS_PREF, 0).getBoolean(LPMLWApplication.SLIDE_WALLPAPER_KEY, false);
        }

        @Override 
        public void onSurfaceCreated(SurfaceHolder surfaceHolder) {
            super.onSurfaceCreated(surfaceHolder);
            try {
                createGLSurfaceView();
                this.renderer.setScreenSize(surfaceHolder.getSurfaceFrame().width(), surfaceHolder.getSurfaceFrame().height());
                startPlayer();
            } catch (Exception unused) {
            }
        }

        @Override 
        public void onVisibilityChanged(boolean z) {
            super.onVisibilityChanged(z);
            if (this.renderer == null) {
                return;
            }
            if (z) {
                this.allowSlide = LPMLWWallpaperService.this.getSharedPreferences(LPMLWApplication.OPTIONS_PREF, 0).getBoolean(LPMLWApplication.SLIDE_WALLPAPER_KEY, false);
                this.glSurfaceView.onResume();
                startPlayer();
                return;
            }
            stopPlayer();
            this.glSurfaceView.onPause();
            this.allowSlide = false;
        }

        @Override 
        public void onOffsetsChanged(float f, float f2, float f3, float f4, int i, int i2) {
            super.onOffsetsChanged(f, f2, f3, f4, i, i2);
            if (this.allowSlide && !isPreview()) {
                this.renderer.setOffset(0.5f - f, 0.5f - f2);
            }
        }

        @Override 
        public void onSurfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
            super.onSurfaceChanged(surfaceHolder, i, i2, i3);
            this.renderer.setScreenSize(i2, i3);
        }

        @Override 
        public void onSurfaceDestroyed(SurfaceHolder surfaceHolder) {
            super.onSurfaceDestroyed(surfaceHolder);
            stopPlayer();
            this.glSurfaceView.onDestroy();
        }

        public void createGLSurfaceView() {
            try {
                GLWallpaperSurfaceView gLWallpaperSurfaceView = this.glSurfaceView;
                if (gLWallpaperSurfaceView != null) {
                    gLWallpaperSurfaceView.onDestroy();
                    this.glSurfaceView = null;
                }
                this.glSurfaceView = new GLWallpaperSurfaceView(this.context);
                ActivityManager activityManager = (ActivityManager) LPMLWWallpaperService.this.getSystemService(Context.ACTIVITY_SERVICE);
                if (activityManager != null) {
                    ConfigurationInfo deviceConfigurationInfo = activityManager.getDeviceConfigurationInfo();
                    if (deviceConfigurationInfo.reqGlEsVersion >= 196608) {
                        wallpaperUtils.debug(TAG, "Support GLESv3");
                        this.glSurfaceView.setEGLContextClientVersion(3);
                        this.renderer = new Wallpaper30Renderer(this.context);
                    } else if (deviceConfigurationInfo.reqGlEsVersion >= 131072) {
                        wallpaperUtils.debug(TAG, "Fallback to GLESv2");
                        this.glSurfaceView.setEGLContextClientVersion(2);
                        this.renderer = new wallpaper20Renderer(this.context);
                    } else {
                        Toast.makeText(this.context, (int) R.string.gles_version, Toast.LENGTH_LONG).show();
                        throw new RuntimeException("Needs GLESv2 or higher");
                    }
                    this.glSurfaceView.setPreserveEGLContextOnPause(true);
                    this.glSurfaceView.setRenderer(this.renderer);
                    this.glSurfaceView.setRenderMode(1);
                    return;
                }
                throw new RuntimeException("Cannot get ActivityManager");
            } catch (Exception unused) {
            }
        }

        private boolean checkWallpaperCardValid() {
            WallpaperCardModel wallpaperCardModel = this.WallpaperCardModel;
            if (wallpaperCardModel == null) {
                return false;
            }
            wallpaperCardModel.getType();
            WallpaperCardModel.Type type = com.demo.livwllpaper.wallpapermodule.WallpaperCardModel.Type.INTERNAL;
            return true;
        }

        public void loadWallpaperCard() {
            this.oldWallpaperCardModel = this.WallpaperCardModel;
            if (isPreview()) {
                this.WallpaperCardModel = LPMLWApplication.getPreviewLPMLWWallpaperCardModel();

            } else {
                this.WallpaperCardModel = LPMLWApplication.getCurrentWallpaperCard(this.context);
            }
            if (!checkWallpaperCardValid()) {
                if (this.WallpaperCardModel != null) {
                    Toast.makeText(this.context, (int) R.string.invalid_path, Toast.LENGTH_LONG).show();
                    this.WallpaperCardModel.setInvalid();
                }
                List<WallpaperCardModel> cards = LPMLWApplication.getCards(this.context);
                if (cards.size() <= 0 || cards.get(0) == null) {
                    this.WallpaperCardModel = null;
                    Toast.makeText(this.context, (int) R.string.default_failed, Toast.LENGTH_LONG).show();
                    throw new RuntimeException("Failed to fallback to internal wallpaper");
                }
                this.WallpaperCardModel = cards.get(0);
            }
        }

        public void getVideoMetadata() throws IOException {
            try {
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                int i = AnonymousClass1.$SwitchMap$com$livwllpaper$loppphtodemo$wallpapermodule$WallpaperCardModel$Type[this.WallpaperCardModel.getType().ordinal()];
                if (i == 1) {
                    AssetFileDescriptor openFd = LPMLWWallpaperService.this.getAssets().openFd(this.WallpaperCardModel.getPath());
                    mediaMetadataRetriever.setDataSource(openFd.getFileDescriptor(), openFd.getStartOffset(), openFd.getDeclaredLength());
                    openFd.close();
                } else if (i == 2) {
                    mediaMetadataRetriever.setDataSource(this.context, this.WallpaperCardModel.getUri());
                }
                String extractMetadata = mediaMetadataRetriever.extractMetadata(24);
                String extractMetadata2 = mediaMetadataRetriever.extractMetadata(18);
                String extractMetadata3 = mediaMetadataRetriever.extractMetadata(19);
                mediaMetadataRetriever.release();
                this.videoRotation = Integer.parseInt(extractMetadata);
                this.videoWidth = Integer.parseInt(extractMetadata2);
                this.videoHeight = Integer.parseInt(extractMetadata3);
            } catch (Exception e) {
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(this.context.getAssets().open(LPMLWApplication.INTERNAL_WALLPAPER_IMAGE_PATH));
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                WallpaperCardModel wallpaperCardModel = new WallpaperCardModel(this.context.getResources().getString(R.string.title_invalid), LPMLWApplication.INTERNAL_WALLPAPER_VIDEO_PATH, Uri.parse("file:///android_asset/asset_1.mp4"), com.demo.livwllpaper.wallpapermodule.WallpaperCardModel.Type.INTERNAL, bitmap);
                this.WallpaperCardModel = wallpaperCardModel;
                this.oldWallpaperCardModel = wallpaperCardModel;
                LPMLWApplication.setCurrentWallpaperCard(this.context, wallpaperCardModel);
                getVideoMetadata();
                Log.e("in_main", "Exception " + e);
            }
        }

        private void startPlayer() {
            if (this.exoPlayer != null) {
                stopPlayer();
            }
            wallpaperUtils.debug(TAG, "Player starting");
            loadWallpaperCard();
            if (this.WallpaperCardModel != null) {
                try {
                    getVideoMetadata();
                    DefaultTrackSelector defaultTrackSelector = new DefaultTrackSelector();
                    this.trackSelector = defaultTrackSelector;
                    SimpleExoPlayer newSimpleInstance = ExoPlayerFactory.newSimpleInstance(this.context, defaultTrackSelector);
                    this.exoPlayer = newSimpleInstance;
                    newSimpleInstance.setVolume(0.0f);
                    int rendererCount = this.exoPlayer.getRendererCount();
                    for (int i = 0; i < rendererCount; i++) {
                        if (this.exoPlayer.getRendererType(i) == 1) {
                            DefaultTrackSelector defaultTrackSelector2 = this.trackSelector;
                            defaultTrackSelector2.setParameters(defaultTrackSelector2.buildUponParameters().setRendererDisabled(i, true));
                        }
                    }
                    this.exoPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);
                    Context context = this.context;
                    this.videoSource = new ExtractorMediaSource.Factory(new DefaultDataSourceFactory(context, Util.getUserAgent(context, context.getPackageName()))).createMediaSource(this.WallpaperCardModel.getUri());
                    this.renderer.setVideoSizeAndRotation(this.videoWidth, this.videoHeight, this.videoRotation);
                    this.renderer.setSourcePlayer(this.exoPlayer);
                    this.exoPlayer.prepare(this.videoSource);
                    WallpaperCardModel WallpaperCardModel = this.oldWallpaperCardModel;
                    if (WallpaperCardModel != null && WallpaperCardModel.equals(this.WallpaperCardModel)) {
                        this.exoPlayer.seekTo(this.progress);
                    }
                    this.exoPlayer.setPlayWhenReady(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void stopPlayer() {
            SimpleExoPlayer simpleExoPlayer = this.exoPlayer;
            if (simpleExoPlayer != null) {
                if (simpleExoPlayer.getPlayWhenReady()) {
                    wallpaperUtils.debug(TAG, "Player stopping");
                    this.exoPlayer.setPlayWhenReady(false);
                    this.progress = this.exoPlayer.getCurrentPosition();
                    this.exoPlayer.stop();
                }
                this.exoPlayer.release();
                this.exoPlayer = null;
            }
            this.videoSource = null;
            this.trackSelector = null;
        }
    }

    
    
    
    public static  class AnonymousClass1 {
        static final  int[] $SwitchMap$com$livwllpaper$loppphtodemo$wallpapermodule$WallpaperCardModel$Type;

        static {
            int[] iArr = new int[WallpaperCardModel.Type.values().length];
            $SwitchMap$com$livwllpaper$loppphtodemo$wallpapermodule$WallpaperCardModel$Type = iArr;
            try {
                iArr[WallpaperCardModel.Type.INTERNAL.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$livwllpaper$loppphtodemo$wallpapermodule$WallpaperCardModel$Type[WallpaperCardModel.Type.EXTERNAL.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    @Override 
    public Engine onCreateEngine() {
        return new GLWallpaperEngine(this);
    }
}