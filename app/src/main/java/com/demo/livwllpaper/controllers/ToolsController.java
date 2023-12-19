package com.demo.livwllpaper.controllers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.demo.livwllpaper.R;
import com.demo.livwllpaper.Utilsx.Utils;
import com.demo.livwllpaper.activities.Exportactivity;
import com.demo.livwllpaper.activities.Windividualhelpviewact;
import com.demo.livwllpaper.beans.Point;
import com.demo.livwllpaper.beans.TriangleBitmap;

import java.text.NumberFormat;


public class ToolsController implements View.OnClickListener {
    public static final int FERRAMENTA_APAGAR_MASCARA = 6;
    public static final int FERRAMENTA_ESTABILIZACAO = 2;
    public static final int FERRAMENTA_MASCARA = 3;
    public static final int FERRAMENTA_MOVIMENTO = 1;
    public static final int FERRAMENTA_MOVIMENTO_SEQUENCIA = 5;
    public static final int FERRAMENTA_SELECAO = 4;
    public static final int FERRAMENTA_ZOOM = 7;
    private static final int INIT_ALPHA_MASK = 150;
    public static final int INIT_COLOR_MASK = -65536;
    private static final int INIT_RAIO_MASK = 20;
    public static final int MAX_TAMANHO_RAIO = 100;
    public static final int MAX_TIME_PREVIEW = 10000;
    public static final int MAX_TIME_PREVIEW_FREE = 6000;
    public static final int MIN_TAMANHO_RAIO = 3;
    public static final int MIN_TIME_PREVIEW = 2000;
    public static final int SEM_FERRAMENTA = 0;
    static ImageView btn_masking_inside_layout;
    static ImageView ic_tutorial_inside_masking;
    static ImageView iv_cancel_rl_brush_settings;
    static ImageView iv_tutorial_main_screen;
    public static RelativeLayout relative_layout_brush_size;
    public static RelativeLayout relative_layout_speed_settings;
    private static ToolsController tools_controller;
    private Activity activity;
    private ImageView btDelete;
    private ImageView btMask;
    private ImageView btMovSequencia;
    private ImageView btZoom;
    private ImageView btn_Erase_Masking;
    private ImageView btn_Stabilize_custom_points;
    private Button btn_cancel;
    private ImageView btn_movement_single_array;
    private ImageView btn_select_remove_points;
    private Point final_selection;
    private Point initial_selection;
    private ImageView iv_bg_Mask;
    private ImageView iv_bg_Stabilize_custom_points;
    private ImageView iv_bg_Zoom;
    private ImageView iv_bg_btMovSequencia;
    private ImageView iv_bg_movement_single_array;
    private ImageView iv_bg_select_remove_points;
    private ImageView iv_erase_everything;
    private ImageView iv_erase_masking;
    private ImageView iv_erase_points;
    private LinearLayout ll_delete_image;
    private LinearLayout ll_eraser_options;
    private LinearLayout ll_for_masking;
    private LinearLayout ll_for_seekbar;
    private LinearLayout ll_tutorial_image;
    private MaskController maskController;
    private Paint masking_paint;
    private Paint masking_paint_representation;
    private Paint paintSelect;
    private int position;
    private SeekBar seekbar_Time_Speed_setting;
    private SeekBar seekbar_brush_size_for_masking;
    private ToolsListener toolsListener;
    TextView tv_erase;
    TextView tv_mask;
    TextView tv_motion;
    TextView tv_rl_brush_settings_header;
    TextView tv_select;
    TextView tv_sequence;
    TextView tv_stabilise;
    TextView tv_zoom;
    private float xAtual;
    private float yAtual;
    private static final int INIT_TAM_MOV_SEQUENCIA = Math.round(64.0f);
    public static final int MAX_SIZE_MOVE_SEQUENCE_ARROW = Math.round(240.00002f);
    public static final int MIN_SIZE_MOVE_SEQUENCE_ARROW = Math.round(24.0f);
    private static float STROKE_INIT = 4.0f;
    private int alphaMask = INIT_ALPHA_MASK;
    private boolean enabled = true;
    private int Current_tool = -1;
    private boolean is_Erasing_Mask = false;
    private boolean is_Masking = false;
    private boolean is_Selecting = false;
    private boolean playingPreview = false;
    private int ray_Mask = 20;
    private boolean showDetalhes = false;
    private int tam_Movement_Sequence = INIT_TAM_MOV_SEQUENCIA;
    private int tempoPreview = 10000;
    private int type_tool = 0;
    public int selected_eraser_option = 2;

    
    public interface ToolsListener {
        void onChange_Size_of_Arrow_Mov_Sequence(int i, Bitmap bitmap);

        void onPlayPreview();

        void onPressDelete();

        void onPressZoom();

        void onPress_Arrow_Movement_in_Sequence();

        void onPress_Clear_Mask();

        void onStopPreview();

        void on_Change_Ray_Mask(int i, Bitmap bitmap);

        void on_Changed_Time(int i);

        void on_Masking_Pressed();

        void on_Press_Selection_button();

        void on_Press_Single_Arrow_Movement();

        void on_Press_Stabilize();

        void on_Time_Changing(int i);
    }

    public void restartDefinitions() {
        this.maskController = new MaskController();
        Paint paint = new Paint(1);
        this.paintSelect = paint;
        paint.setFilterBitmap(true);
        this.paintSelect.setStyle(Paint.Style.STROKE);
        this.paintSelect.setColor(-1);
        this.paintSelect.setStrokeWidth(STROKE_INIT);
        Paint paint2 = new Paint(1);
        this.masking_paint = paint2;
        paint2.setFilterBitmap(true);
        this.masking_paint.setStyle(Paint.Style.STROKE);
        this.masking_paint.setColor(-1);
        this.masking_paint.setStrokeWidth(STROKE_INIT);
        Paint paint3 = new Paint(1);
        this.masking_paint_representation = paint3;
        paint3.setFilterBitmap(true);
        this.masking_paint_representation.setStyle(Paint.Style.FILL);
        this.masking_paint_representation.setAlpha(this.maskController.getAlpha());
        this.masking_paint_representation.setColor(this.maskController.getColor());
        ImageView imageView = (ImageView) this.activity.findViewById(R.id.btn_movement_single_array);
        this.btn_movement_single_array = imageView;
        imageView.setEnabled(false);
        this.btn_movement_single_array.setImageResource(R.drawable.single_motion_press);
        this.btn_movement_single_array.setOnClickListener(this);
        ImageView imageView2 = (ImageView) this.activity.findViewById(R.id.btn_movement_sequence_arrow);
        this.btMovSequencia = imageView2;
        imageView2.setEnabled(false);
        this.btMovSequencia.setImageResource(R.drawable.sequence);
        this.btMovSequencia.setOnClickListener(this);
        ImageView imageView3 = (ImageView) this.activity.findViewById(R.id.btn_select_remove_points);
        this.btn_select_remove_points = imageView3;
        imageView3.setEnabled(false);
        this.btn_select_remove_points.setImageResource(R.drawable.select);
        this.btn_select_remove_points.setOnClickListener(this);
        ImageView imageView4 = (ImageView) this.activity.findViewById(R.id.btZoom);
        this.btZoom = imageView4;
        imageView4.setEnabled(false);
        this.btZoom.setImageResource(R.drawable.zoom_in);
        this.btZoom.setOnClickListener(this);
        ImageView imageView5 = (ImageView) this.activity.findViewById(R.id.btn_Stabilize_custom_points);
        this.btn_Stabilize_custom_points = imageView5;
        imageView5.setEnabled(false);
        this.btn_Stabilize_custom_points.setImageResource(R.drawable.stabilise);
        this.btn_Stabilize_custom_points.setOnClickListener(this);
        ImageView imageView6 = (ImageView) this.activity.findViewById(R.id.btMask);
        this.btMask = imageView6;
        imageView6.setEnabled(false);
        this.btMask.setImageResource(R.drawable.mask_press);
        this.btMask.setOnClickListener(this);
        ImageView imageView7 = (ImageView) this.activity.findViewById(R.id.btn_Erase_Masking);
        this.btn_Erase_Masking = imageView7;
        imageView7.setEnabled(false);
        this.btn_Erase_Masking.setOnClickListener(this);
        this.iv_erase_everything = (ImageView) this.activity.findViewById(R.id.iv_erase_everything);
        this.iv_erase_points = (ImageView) this.activity.findViewById(R.id.iv_erase_points);
        this.iv_erase_masking = (ImageView) this.activity.findViewById(R.id.iv_erase_masking);
        this.iv_erase_everything.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                Exportactivity.editor.putInt("selected_eraser_option", 2);
                Exportactivity.editor.commit();
                ToolsController.this.iv_erase_everything.setImageResource(R.drawable.erase_all_filled_bg);
                ToolsController.this.iv_erase_points.setImageResource(R.drawable.erase_motionunfilled_bg);
                ToolsController.this.iv_erase_masking.setImageResource(R.drawable.erase_mask_unfilled_bg);
            }
        });
        this.iv_erase_points.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                Exportactivity.editor.putInt("selected_eraser_option", 1);
                Exportactivity.editor.commit();
                ToolsController.this.iv_erase_points.setImageResource(R.drawable.erase_motionfilled_bg);
                ToolsController.this.iv_erase_everything.setImageResource(R.drawable.erase_all_unfilled_bg);
                ToolsController.this.iv_erase_masking.setImageResource(R.drawable.erase_mask_unfilled_bg);
            }
        });
        this.iv_erase_masking.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                Exportactivity.editor.putInt("selected_eraser_option", 0);
                Exportactivity.editor.commit();
                ToolsController.this.iv_erase_masking.setImageResource(R.drawable.erase_mask_filled_bg);
                ToolsController.this.iv_erase_points.setImageResource(R.drawable.erase_motionunfilled_bg);
                ToolsController.this.iv_erase_everything.setImageResource(R.drawable.erase_all_unfilled_bg);
            }
        });
        this.btn_Erase_Masking.setOnLongClickListener(new View.OnLongClickListener() { 
            @Override 
            public boolean onLongClick(View view) {
                ToolsController.this.on_unselected();
                ToolsController.this.tv_erase.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                ToolsController.this.tv_erase.setSelected(true);
                ToolsController.this.tv_erase.setSingleLine(true);
                ToolsController.this.delete_selection();
                ToolsController.this.on_effect_applied();
                ToolsController.this.closePreview();
                ToolsController.this.is_Erasing_Mask = true;
                ToolsController.this.type_tool = 6;
                ToolsController.relative_layout_brush_size.setVisibility(View.VISIBLE);
                ToolsController.this.ll_for_masking.setVisibility(View.VISIBLE);
                ToolsController.ic_tutorial_inside_masking.setVisibility(View.VISIBLE);
                ToolsController.this.btn_Erase_Masking.setVisibility(View.VISIBLE);
                ToolsController.btn_masking_inside_layout.setVisibility(View.VISIBLE);
                ToolsController.this.tv_rl_brush_settings_header.setText(ToolsController.this.activity.getResources().getString(R.string.tools_text_delete_masking));
                ToolsController.this.ll_for_seekbar.setVisibility(View.VISIBLE);
                ToolsController.this.ll_delete_image.setVisibility(View.GONE);
                ToolsController.this.ll_tutorial_image.setVisibility(View.GONE);
                ToolsController.this.ll_eraser_options.setVisibility(View.VISIBLE);
                ToolsController.this.btn_Erase_Masking.setImageResource(R.drawable.erasebtn_filled_bg);
                ToolsController.this.seekbar_brush_size_for_masking.setMax(97);
                ToolsController.this.seekbar_brush_size_for_masking.setProgress(ToolsController.this.ray_Mask - 3);
                ToolsController.this.toolsListener.onPress_Clear_Mask();
                ToolsController.this.toolsListener.on_Change_Ray_Mask(ToolsController.get_Ray_Mask(), ToolsController.this.get_Radius_Representation());
                ToolsController.this.selected_eraser_option = Exportactivity.prefs_eraser.getInt("selected_eraser_option", 2);
                int i = ToolsController.this.selected_eraser_option;
                if (i == 0) {
                    ToolsController.this.iv_erase_masking.setImageResource(R.drawable.erase_mask_filled_bg);
                } else if (i == 1) {
                    ToolsController.this.iv_erase_points.setImageResource(R.drawable.erase_motionfilled_bg);
                } else if (i == 2) {
                    ToolsController.this.iv_erase_everything.setImageResource(R.drawable.erase_all_filled_bg);
                }
                return true;
            }
        });
        ImageView imageView8 = (ImageView) this.activity.findViewById(R.id.btDelete);
        this.btDelete = imageView8;
        imageView8.setVisibility(View.GONE);
        this.btDelete.setOnClickListener(this);
        SeekBar seekBar = (SeekBar) this.activity.findViewById(R.id.seekbar_brush_size_for_masking);
        this.seekbar_brush_size_for_masking = seekBar;
        seekBar.setOnSeekBarChangeListener(new seekbar_brush_size_listener());
        SeekBar seekBar2 = (SeekBar) this.activity.findViewById(R.id.seekbar_Time_Speed_setting);
        this.seekbar_Time_Speed_setting = seekBar2;
        seekBar2.setMax(8000);
        this.seekbar_Time_Speed_setting.setOnSeekBarChangeListener(new seekbar_speed_settings_listener());
        RelativeLayout relativeLayout = (RelativeLayout) this.activity.findViewById(R.id.relative_layout_brush_size);
        relative_layout_brush_size = relativeLayout;
        relativeLayout.setVisibility(View.GONE);
        RelativeLayout relativeLayout2 = (RelativeLayout) this.activity.findViewById(R.id.relative_layout_speed_settings);
        relative_layout_speed_settings = relativeLayout2;
        relativeLayout2.setVisibility(View.GONE);
        iv_tutorial_main_screen = (ImageView) this.activity.findViewById(R.id.iv_tutorial_main_screen);
        iv_cancel_rl_brush_settings = (ImageView) this.activity.findViewById(R.id.iv_cancel_rl_brush_settings);
        this.tv_rl_brush_settings_header = (TextView) this.activity.findViewById(R.id.tv_rl_brush_settings_header);
        this.ll_for_seekbar = (LinearLayout) this.activity.findViewById(R.id.ll_for_seekbar);
        this.ll_tutorial_image = (LinearLayout) this.activity.findViewById(R.id.ll_tutorial_image);
        this.ll_delete_image = (LinearLayout) this.activity.findViewById(R.id.ll_delete_image);
        ic_tutorial_inside_masking = (ImageView) this.activity.findViewById(R.id.ic_tutorial_inside_masking);
        this.ll_for_masking = (LinearLayout) this.activity.findViewById(R.id.ll_for_masking);
        this.ll_eraser_options = (LinearLayout) this.activity.findViewById(R.id.ll_eraser_options);
        btn_masking_inside_layout = (ImageView) this.activity.findViewById(R.id.btn_masking_inside_layout);
        this.iv_bg_Stabilize_custom_points = (ImageView) this.activity.findViewById(R.id.iv_bg_Stabilize_custom_points);
        this.iv_bg_Mask = (ImageView) this.activity.findViewById(R.id.iv_bg_Mask);
        this.iv_bg_movement_single_array = (ImageView) this.activity.findViewById(R.id.iv_bg_movement_single_array);
        this.iv_bg_btMovSequencia = (ImageView) this.activity.findViewById(R.id.iv_bg_movement_sequence_arrow);
        this.iv_bg_select_remove_points = (ImageView) this.activity.findViewById(R.id.iv_bg_select_remove_points);
        this.iv_bg_Zoom = (ImageView) this.activity.findViewById(R.id.iv_bg_Zoom);
        this.maskController.clearMask();
        this.maskController.setAlpha(this.alphaMask);
        this.playingPreview = false;
        this.tv_sequence = (TextView) this.activity.findViewById(R.id.tv_sequence);
        this.tv_motion = (TextView) this.activity.findViewById(R.id.tv_motion);
        this.tv_stabilise = (TextView) this.activity.findViewById(R.id.tv_stabilise);
        this.tv_select = (TextView) this.activity.findViewById(R.id.tv_select);
        this.tv_mask = (TextView) this.activity.findViewById(R.id.tv_mask);
        this.tv_zoom = (TextView) this.activity.findViewById(R.id.tv_zoom);
        this.tv_erase = (TextView) this.activity.findViewById(R.id.tv_erase);
        iv_cancel_rl_brush_settings.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                ToolsController.relative_layout_brush_size.setVisibility(View.GONE);
                ToolsController.relative_layout_speed_settings.setVisibility(View.GONE);
            }
        });
        iv_tutorial_main_screen.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                Intent intent = new Intent(ToolsController.this.activity, Windividualhelpviewact.class);
                intent.putExtra("pos", ToolsController.this.type_tool);
                ToolsController.this.activity.startActivity(intent);
            }
        });
        ic_tutorial_inside_masking.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                Intent intent = new Intent(ToolsController.this.activity, Windividualhelpviewact.class);
                intent.putExtra("pos", ToolsController.this.type_tool);
                ToolsController.this.activity.startActivity(intent);
            }
        });
        btn_masking_inside_layout.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                ToolsController.this.masking_effect();
            }
        });
    }

    @Override 
    public void onClick(View view) {
        relative_layout_brush_size.setVisibility(View.GONE);
        relative_layout_speed_settings.setVisibility(View.GONE);
        ic_tutorial_inside_masking.setVisibility(View.GONE);
        this.ll_for_masking.setVisibility(View.GONE);
        btn_masking_inside_layout.setVisibility(View.GONE);
        this.ll_delete_image.setVisibility(View.GONE);
        this.ll_tutorial_image.setVisibility(View.VISIBLE);
        this.ll_for_seekbar.setVisibility(View.GONE);
        this.is_Erasing_Mask = false;
        int id = view.getId();
        if (id != R.id.btn_select_remove_points) {
            switch (id) {
                case R.id.btDelete :
                    closePreview();
                    on_effect_applied();
                    this.btn_select_remove_points.setColorFilter(ContextCompat.getColor(this.activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                    this.iv_bg_select_remove_points.setBackgroundColor(this.activity.getResources().getColor(R.color.color_white));
                    this.toolsListener.onPressDelete();
                    return;
                case R.id.btMask :
                    masking_effect();
                    return;
                case R.id.btZoom :
                    on_unselected();
                    this.tv_zoom.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    this.tv_zoom.setSelected(true);
                    this.tv_zoom.setSingleLine(true);
                    delete_selection();
                    on_effect_applied();
                    this.btZoom.setColorFilter(ContextCompat.getColor(this.activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                    this.iv_bg_Zoom.setBackgroundColor(this.activity.getResources().getColor(R.color.color_white));
                    closePreview();
                    this.type_tool = 7;
                    relative_layout_brush_size.setVisibility(View.VISIBLE);
                    this.tv_rl_brush_settings_header.setText(this.activity.getResources().getString(R.string.tools_text_zoom));
                    this.ll_for_seekbar.setVisibility(View.GONE);
                    this.ll_delete_image.setVisibility(View.GONE);
                    this.toolsListener.onPressZoom();
                    return;
                case R.id.btn_Erase_Masking :
                    on_unselected();
                    this.tv_erase.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    this.tv_erase.setSelected(true);
                    this.tv_erase.setSingleLine(true);
                    delete_selection();
                    on_effect_applied();
                    closePreview();
                    this.is_Erasing_Mask = true;
                    this.type_tool = 6;
                    relative_layout_brush_size.setVisibility(View.VISIBLE);
                    this.ll_for_masking.setVisibility(View.VISIBLE);
                    ic_tutorial_inside_masking.setVisibility(View.VISIBLE);
                    this.btn_Erase_Masking.setVisibility(View.VISIBLE);
                    btn_masking_inside_layout.setVisibility(View.VISIBLE);
                    this.tv_rl_brush_settings_header.setText(this.activity.getResources().getString(R.string.tools_text_delete_masking));
                    this.ll_for_seekbar.setVisibility(View.VISIBLE);
                    this.ll_delete_image.setVisibility(View.GONE);
                    this.ll_tutorial_image.setVisibility(View.GONE);
                    this.ll_eraser_options.setVisibility(View.GONE);
                    this.btn_Erase_Masking.setImageResource(R.drawable.erasebtn_filled_bg);
                    this.seekbar_brush_size_for_masking.setMax(97);
                    this.seekbar_brush_size_for_masking.setProgress(this.ray_Mask - 3);
                    this.toolsListener.onPress_Clear_Mask();
                    this.toolsListener.on_Change_Ray_Mask(get_Ray_Mask(), get_Radius_Representation());
                    return;
                case R.id.btn_Stabilize_custom_points :
                    delete_selection();
                    on_effect_applied();
                    on_unselected();
                    this.tv_stabilise.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    this.tv_stabilise.setSelected(true);
                    this.tv_stabilise.setSingleLine(true);
                    this.tv_stabilise.setTextColor(ContextCompat.getColor(this.activity, R.color.txt_color_filled));
                    this.btn_Stabilize_custom_points.setColorFilter(ContextCompat.getColor(this.activity, R.color.color_white), PorterDuff.Mode.SRC_IN);
                    this.iv_bg_Stabilize_custom_points.setBackground(this.activity.getResources().getDrawable(R.drawable.bg_view_selected));
                    closePreview();
                    this.type_tool = 2;
                    relative_layout_brush_size.setVisibility(View.VISIBLE);
                    this.tv_rl_brush_settings_header.setText(this.activity.getResources().getString(R.string.tools_text_stabilise));
                    this.ll_for_seekbar.setVisibility(View.GONE);
                    this.ll_delete_image.setVisibility(View.GONE);
                    this.toolsListener.on_Press_Stabilize();
                    return;
                default:
                    switch (id) {
                        case R.id.btn_movement_sequence_arrow :
                            delete_selection();
                            sequence_tool_selected_by_default();
                            return;
                        case R.id.btn_movement_single_array :
                            delete_selection();
                            on_effect_applied();
                            on_unselected();
                            this.tv_motion.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                            this.tv_motion.setSelected(true);
                            this.tv_motion.setSingleLine(true);
                            this.tv_motion.setTextColor(ContextCompat.getColor(this.activity, R.color.txt_color_filled));
                            this.btn_movement_single_array.setColorFilter(ContextCompat.getColor(this.activity, R.color.color_white), PorterDuff.Mode.SRC_IN);
                            this.iv_bg_movement_single_array.setBackground(this.activity.getResources().getDrawable(R.drawable.bg_view_selected));
                            closePreview();
                            this.type_tool = 1;
                            SeekBar seekBar = this.seekbar_brush_size_for_masking;
                            int i = MAX_SIZE_MOVE_SEQUENCE_ARROW;
                            int i2 = MIN_SIZE_MOVE_SEQUENCE_ARROW;
                            seekBar.setMax(i - i2);
                            this.seekbar_brush_size_for_masking.setProgress(this.tam_Movement_Sequence - i2);
                            relative_layout_brush_size.setVisibility(View.VISIBLE);
                            this.tv_rl_brush_settings_header.setText(this.activity.getResources().getString(R.string.tools_text_motion));
                            this.ll_for_seekbar.setVisibility(View.GONE);
                            this.ll_delete_image.setVisibility(View.GONE);
                            this.toolsListener.on_Press_Single_Arrow_Movement();
                            return;
                        default:
                            this.type_tool = 0;
                            return;
                    }
            }
        } else {
            on_effect_applied();
            on_unselected();
            this.tv_select.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.tv_select.setSelected(true);
            this.tv_select.setSingleLine(true);
            this.btn_select_remove_points.setColorFilter(ContextCompat.getColor(this.activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
            this.iv_bg_select_remove_points.setBackgroundColor(this.activity.getResources().getColor(R.color.color_white));
            closePreview();
            this.type_tool = 4;
            relative_layout_brush_size.setVisibility(View.VISIBLE);
            this.tv_rl_brush_settings_header.setText(this.activity.getResources().getString(R.string.tools_text_select_remove));
            this.ll_for_seekbar.setVisibility(View.GONE);
            this.toolsListener.on_Press_Selection_button();
        }
    }

    public void on_unselected() {
        this.tv_motion.setSelected(false);
        this.tv_sequence.setSelected(false);
        this.tv_stabilise.setSelected(false);
        this.tv_select.setSelected(false);
        this.tv_mask.setSelected(false);
        this.tv_zoom.setSelected(false);
        this.tv_erase.setSelected(false);
    }

    public void on_effect_applied() {
        this.btn_movement_single_array.setImageResource(R.drawable.single_motion_press);
        this.btMovSequencia.setImageResource(R.drawable.sequence);
        this.btn_Stabilize_custom_points.setImageResource(R.drawable.stabilise);
        this.btn_select_remove_points.setImageResource(R.drawable.select);
        this.btMask.setImageResource(R.drawable.mask_press);
        this.btn_Erase_Masking.setImageResource(R.drawable.erasebtn_unfilled_bg);
        this.btZoom.setImageResource(R.drawable.zoom_in);
        this.btn_movement_single_array.setColorFilter(ContextCompat.getColor(this.activity, R.color.txt_color_unfilled), PorterDuff.Mode.SRC_IN);
        this.btMovSequencia.setColorFilter(ContextCompat.getColor(this.activity, R.color.txt_color_unfilled), PorterDuff.Mode.SRC_IN);
        this.btn_Stabilize_custom_points.setColorFilter(ContextCompat.getColor(this.activity, R.color.txt_color_unfilled), PorterDuff.Mode.SRC_IN);
        this.btn_select_remove_points.setColorFilter(ContextCompat.getColor(this.activity, R.color.txt_color_unfilled), PorterDuff.Mode.SRC_IN);
        this.btMask.setColorFilter(ContextCompat.getColor(this.activity, R.color.txt_color_unfilled), PorterDuff.Mode.SRC_IN);
        this.btZoom.setColorFilter(ContextCompat.getColor(this.activity, R.color.txt_color_unfilled), PorterDuff.Mode.SRC_IN);
        this.tv_motion.setTextColor(ContextCompat.getColor(this.activity, R.color.txt_color_unfilled));
        this.tv_sequence.setTextColor(ContextCompat.getColor(this.activity, R.color.txt_color_unfilled));
        this.tv_stabilise.setTextColor(ContextCompat.getColor(this.activity, R.color.txt_color_unfilled));
        this.tv_mask.setTextColor(ContextCompat.getColor(this.activity, R.color.txt_color_unfilled));
        this.iv_bg_Mask.setBackground(this.activity.getResources().getDrawable(R.drawable.bg_view_unselected));
        this.iv_bg_movement_single_array.setBackground(this.activity.getResources().getDrawable(R.drawable.bg_view_unselected));
        this.iv_bg_btMovSequencia.setBackground(this.activity.getResources().getDrawable(R.drawable.bg_view_unselected));
        this.iv_bg_select_remove_points.setBackground(this.activity.getResources().getDrawable(R.drawable.bg_view_unselected));
        this.iv_bg_Stabilize_custom_points.setBackground(this.activity.getResources().getDrawable(R.drawable.bg_view_unselected));
        this.iv_bg_Zoom.setBackground(this.activity.getResources().getDrawable(R.drawable.bg_view_unselected));
        this.ll_eraser_options.setVisibility(View.GONE);
        this.btn_Erase_Masking.setImageResource(R.drawable.erasebtn_unfilled_bg);
        this.iv_erase_masking.setImageResource(R.drawable.erase_mask_unfilled_bg);
        this.iv_erase_points.setImageResource(R.drawable.erase_motionunfilled_bg);
        this.iv_erase_everything.setImageResource(R.drawable.erase_all_unfilled_bg);
    }

    public void sequence_tool_selected_by_default() {
        on_effect_applied();
        on_unselected();
        this.tv_sequence.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        this.tv_sequence.setSelected(true);
        this.tv_sequence.setSingleLine(true);
        this.tv_sequence.setTextColor(ContextCompat.getColor(this.activity, R.color.txt_color_filled));
        this.btMovSequencia.setColorFilter(ContextCompat.getColor(this.activity, R.color.color_white), PorterDuff.Mode.SRC_IN);
        this.iv_bg_btMovSequencia.setBackground(this.activity.getResources().getDrawable(R.drawable.bg_view_selected));
        closePreview();
        this.type_tool = 5;
        relative_layout_brush_size.setVisibility(View.VISIBLE);
        this.tv_rl_brush_settings_header.setText(this.activity.getResources().getString(R.string.tools_text_sequence_motion));
        this.ll_for_seekbar.setVisibility(View.VISIBLE);
        this.ll_delete_image.setVisibility(View.GONE);
        SeekBar seekBar = this.seekbar_brush_size_for_masking;
        int i = MAX_SIZE_MOVE_SEQUENCE_ARROW;
        int i2 = MIN_SIZE_MOVE_SEQUENCE_ARROW;
        seekBar.setMax(i - i2);
        this.seekbar_brush_size_for_masking.setProgress(this.tam_Movement_Sequence - i2);
        this.toolsListener.onPress_Arrow_Movement_in_Sequence();
    }

    public void masking_effect() {
        relative_layout_brush_size.setVisibility(View.VISIBLE);
        this.ll_for_masking.setVisibility(View.VISIBLE);
        ic_tutorial_inside_masking.setVisibility(View.VISIBLE);
        this.btn_Erase_Masking.setVisibility(View.VISIBLE);
        btn_masking_inside_layout.setVisibility(View.VISIBLE);
        this.tv_rl_brush_settings_header.setText(this.activity.getResources().getString(R.string.tools_text_mask));
        this.ll_for_seekbar.setVisibility(View.VISIBLE);
        this.ll_delete_image.setVisibility(View.GONE);
        this.ll_tutorial_image.setVisibility(View.GONE);
        on_unselected();
        this.tv_mask.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        this.tv_mask.setSelected(true);
        this.tv_mask.setSingleLine(true);
        delete_selection();
        on_effect_applied();
        this.btn_Erase_Masking.setImageResource(R.drawable.erasebtn_unfilled_bg);
        btn_masking_inside_layout.setImageResource(R.drawable.ic_brush_modify_when_clicked);
        this.tv_mask.setTextColor(ContextCompat.getColor(this.activity, R.color.txt_color_filled));
        this.btMask.setColorFilter(ContextCompat.getColor(this.activity, R.color.color_white), PorterDuff.Mode.SRC_IN);
        this.iv_bg_Mask.setBackground(this.activity.getResources().getDrawable(R.drawable.bg_view_selected));
        closePreview();
        this.type_tool = 3;
        this.seekbar_brush_size_for_masking.setMax(97);
        this.seekbar_brush_size_for_masking.setProgress(this.ray_Mask - 3);
        this.toolsListener.on_Masking_Pressed();
        this.toolsListener.on_Change_Ray_Mask(get_Ray_Mask(), get_Radius_Representation());
    }

    
    public class seekbar_brush_size_listener implements SeekBar.OnSeekBarChangeListener {
        @Override 
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override 
        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            if (z) {
                int i2 = ToolsController.this.type_tool;
                if (i2 == 3) {
                    ToolsController.this.ray_Mask = i + 3;
                    ToolsController.this.toolsListener.on_Change_Ray_Mask(ToolsController.this.ray_Mask, ToolsController.this.get_Radius_Representation());
                } else if (i2 == 5) {
                    ToolsController.this.tam_Movement_Sequence = ToolsController.MIN_SIZE_MOVE_SEQUENCE_ARROW + i;
                    ToolsController.this.toolsListener.onChange_Size_of_Arrow_Mov_Sequence(ToolsController.this.tam_Movement_Sequence, ToolsController.this.get_set_Representation());
                } else if (i2 == 6) {
                    ToolsController.this.ray_Mask = i + 3;
                    ToolsController.this.toolsListener.on_Change_Ray_Mask(ToolsController.this.ray_Mask, ToolsController.this.get_Radius_Representation());
                }
            }
        }

        @Override 
        public void onStopTrackingTouch(SeekBar seekBar) {
            ToolsController.this.toolsListener.onChange_Size_of_Arrow_Mov_Sequence(ToolsController.this.tam_Movement_Sequence, null);
        }
    }

    
    public class seekbar_speed_settings_listener implements SeekBar.OnSeekBarChangeListener {
        @Override 
        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        }

        @Override 
        public void onStartTrackingTouch(SeekBar seekBar) {
        }


        @Override 
        public void onStopTrackingTouch(SeekBar seekBar) {
            int progress = seekBar.getProgress();
            ToolsController.this.tempoPreview = 10000 - Math.round((((float) progress) / ((float) seekBar.getMax())) * 8000.0f);
            NumberFormat instance = NumberFormat.getInstance();
            instance.setMaximumFractionDigits(1);
            Bitmap createBitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            Paint paint = new Paint(1);
            paint.setFilterBitmap(true);
            paint.setColor(ResourcesCompat.getColor(ToolsController.this.activity.getResources(), R.color.colorAccent, null));
            paint.setStyle(Paint.Style.FILL);
            float width = (float) (createBitmap.getWidth() / 2);
            float height = (float) (createBitmap.getHeight() / 2);
            canvas.drawCircle(width, height, (float) ((canvas.getHeight() / 2) - 2), paint);
            paint.setColor(-1);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(4.0f);
            canvas.drawCircle(width, height, (float) ((canvas.getHeight() / 2) - 2), paint);
            Paint paint2 = new Paint(1);
            paint2.setColor(-1);
            paint2.setTextAlign(Paint.Align.CENTER);
            paint2.setTextSize(60.0f);
            paint2.setFakeBoldText(true);
            canvas.drawText(instance.format((double) (((float) ToolsController.this.tempoPreview) / 1000.0f)) + "s", (float) Math.round(((float) canvas.getWidth()) / 2.0f), (float) Math.round((((float) canvas.getHeight()) / 2.0f) - ((paint2.descent() + paint2.ascent()) / 2.0f)), paint2);
            ToolsController.this.toolsListener.on_Time_Changing(ToolsController.this.tempoPreview);
            ToolsController.this.toolsListener.on_Changed_Time(ToolsController.this.tempoPreview);
        }
    }

    public void set_speed_for_preview(int i) {
        this.toolsListener.on_Time_Changing(i);
    }

    public void updated_speed_for_preview(int i) {
        this.toolsListener.on_Changed_Time(i);
    }

    
    public class MaskController {
        private static final String BUNDLE_MASK = "BUNDLE_MASK";
        private Canvas canvasMask;
        private Bitmap mask;
        private Paint paintMask = new Paint(1);
        private Paint paintRepresentacaoMask;

        public void setMask(Bitmap bitmap) {
            this.mask = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            this.canvasMask = new Canvas(this.mask);
        }

        protected MaskController() {
            Paint paint = new Paint(1);
            this.paintRepresentacaoMask = paint;
            paint.setAntiAlias(true);
            this.paintRepresentacaoMask.setFilterBitmap(true);
            this.paintRepresentacaoMask.setStyle(Paint.Style.FILL);
            this.paintRepresentacaoMask.setAlpha(ToolsController.INIT_ALPHA_MASK);
            this.paintRepresentacaoMask.setColor(-65536);
            this.paintRepresentacaoMask.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
            this.paintMask.setStyle(Paint.Style.FILL);
            this.paintMask.setFilterBitmap(true);
            this.paintMask.setColor(-65536);
        }

        public void pintarCor(Bitmap bitmap) {
            if (this.mask != null) {
                new Canvas(bitmap).drawBitmap(this.mask, 0.0f, 0.0f, this.paintRepresentacaoMask);
            }
        }

        public void addMask(Bitmap bitmap) {
            if (this.mask == null) {
                this.mask = Bitmap.createBitmap(AnimationController.getImagemWidth(), AnimationController.getImagemHeight(), Bitmap.Config.ARGB_8888);
                this.canvasMask = new Canvas(this.mask);
            }
            this.paintMask.setXfermode(null);
            this.canvasMask.drawBitmap(bitmap, 0.0f, 0.0f, this.paintMask);
        }

        public Bitmap addMask(float f, float f2, float f3) {
            if (this.mask == null) {
                this.mask = Bitmap.createBitmap(AnimationController.getImagemWidth(), AnimationController.getImagemHeight(), Bitmap.Config.ARGB_8888);
                this.canvasMask = new Canvas(this.mask);
            }
            Bitmap createBitmap = Bitmap.createBitmap(this.mask.getWidth(), this.mask.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            this.paintMask.setXfermode(null);
            canvas.drawCircle(f, f2, ((float) ToolsController.get_Ray_Mask()) * 0.2f, this.paintMask);
            this.paintMask.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
            canvas.drawBitmap(this.mask, 0.0f, 0.0f, this.paintMask);
            this.paintMask.setXfermode(null);
            this.canvasMask.drawCircle(f, f2, ((float) ToolsController.get_Ray_Mask()) * (1.0f / f3), this.paintMask);
            return createBitmap;
        }

        public void deleteMask(Bitmap bitmap) {
            if (this.mask != null && bitmap != null) {
                this.paintMask.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
                this.canvasMask.drawBitmap(bitmap, 0.0f, 0.0f, this.paintMask);
            }
        }

        public Bitmap delete_only_points_masking(float f, float f2, float f3) {
            Bitmap bitmap = this.mask;
            if (bitmap == null) {
                return null;
            }
            Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), this.mask.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            canvas.drawCircle(f, f2, ((float) ToolsController.get_Ray_Mask()) * (1.0f / f3), this.paintMask);
            this.paintMask.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            canvas.drawBitmap(this.mask, 0.0f, 0.0f, this.paintMask);
            this.paintMask.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            return createBitmap;
        }

        public Bitmap deleteMask(float f, float f2, float f3) {
            Bitmap bitmap = this.mask;
            if (bitmap == null) {
                return null;
            }
            Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), this.mask.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            float f4 = 1.0f / f3;
            canvas.drawCircle(f, f2, ((float) ToolsController.get_Ray_Mask()) * f4, this.paintMask);
            this.paintMask.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            canvas.drawBitmap(this.mask, 0.0f, 0.0f, this.paintMask);
            this.paintMask.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            this.canvasMask.drawCircle(f, f2, ((float) ToolsController.get_Ray_Mask()) * f4, this.paintMask);
            return createBitmap;
        }

        public Bitmap deleteMaskZoom(float f, float f2, float f3) {
            Bitmap bitmap = this.mask;
            if (bitmap == null) {
                return null;
            }
            Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), this.mask.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            float f4 = 1.1f / f3;
            canvas.drawCircle(f, f2, ((float) ToolsController.get_Ray_Mask()) * f4, this.paintMask);
            this.paintMask.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            canvas.drawBitmap(this.mask, 0.0f, 0.0f, this.paintMask);
            this.paintMask.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            this.canvasMask.drawCircle(f, f2, ((float) ToolsController.get_Ray_Mask()) * f4, this.paintMask);
            return createBitmap;
        }

        public void setColor(int i) {
            this.paintRepresentacaoMask.setColor(i);
        }

        public int getColor() {
            return this.paintRepresentacaoMask.getColor();
        }

        public void setAlpha(int i) {
            this.paintRepresentacaoMask.setAlpha(i);
        }

        public int getAlpha() {
            return this.paintRepresentacaoMask.getAlpha();
        }

        public void clearMask() {
            Bitmap bitmap = this.mask;
            if (bitmap != null) {
                bitmap.recycle();
                this.mask = null;
            }
        }
    }

    
    public class Iterate_points_listener implements AnimationController.Iterator_Of_Point {

        @Override 
        public void onIterate(Point lPMLWPoint) {
            lPMLWPoint.set_Selected(false);
        }
    }

    public static ToolsController init(Activity activity) {
        ToolsController lPMLWToolsController = new ToolsController();
        tools_controller = lPMLWToolsController;
        lPMLWToolsController.activity = activity;
        lPMLWToolsController.restartDefinitions();
        Utils.is_arrow_movement = false;
        ToolsController lPMLWToolsController2 = tools_controller;
        if (lPMLWToolsController2.type_tool != 0) {
            lPMLWToolsController2.set_Current_Tool(lPMLWToolsController2.Current_tool);
        }
        return tools_controller;
    }

    private ToolsController() {
    }

    private void set_Current_Tool(int i) {
        ImageButton imageButton = (ImageButton) this.activity.findViewById(i);
        this.Current_tool = i;
    }

    public void set_selecting(boolean z) {
        this.is_Selecting = z;
    }

    public void set_Masking(boolean z) {
        this.is_Masking = z;
    }

    public void delete_selection() {
        AnimationController.getInstance().iterate_Points(new Iterate_points_listener());
        this.btDelete.setVisibility(View.GONE);
    }

    public void set_selecting(final Point lPMLWPoint, Point lPMLWPoint2) {
        this.initial_selection = lPMLWPoint;
        this.final_selection = lPMLWPoint2;
        final Rect rect = new Rect(Math.round(lPMLWPoint2.getXInit() < lPMLWPoint.getXInit() ? lPMLWPoint2.getXInit() : lPMLWPoint.getXInit()), Math.round(lPMLWPoint2.getYInit() < lPMLWPoint.getYInit() ? lPMLWPoint2.getYInit() : lPMLWPoint.getYInit()), Math.round(lPMLWPoint2.getXInit() > lPMLWPoint.getXInit() ? lPMLWPoint2.getXInit() : lPMLWPoint.getXInit()), Math.round(lPMLWPoint2.getYInit() > lPMLWPoint.getYInit() ? lPMLWPoint2.getYInit() : lPMLWPoint.getYInit()));
        this.is_Selecting = true;
        AnimationController.getInstance().iterate_Points(new AnimationController.Iterator_Of_Point() { 
            @Override 
            public void onIterate(Point lPMLWPoint3) {
                lPMLWPoint3.set_Selected(rect.contains(Math.round(lPMLWPoint3.getXInit()), Math.round(lPMLWPoint3.getYInit())));
            }
        });
        if (lPMLWPoint.distance_to(lPMLWPoint2) <= 24.0d) {
            AnimationController.getInstance().iterate_Points(new AnimationController.Iterator_Of_Point() { 
                @Override 
                public void onIterate(Point lPMLWPoint3) {
                    if (lPMLWPoint3.in_Circumference(lPMLWPoint, 24.0d)) {
                        lPMLWPoint3.set_Selected(true);
                    }
                }
            });
        }
    }

    public void setDeleteToolVisibility(boolean z) {
        if (z) {
            this.ll_delete_image.setVisibility(View.VISIBLE);
            this.btDelete.setVisibility(View.VISIBLE);
            return;
        }
        this.ll_delete_image.setVisibility(View.GONE);
        this.btDelete.setVisibility(View.GONE);
    }

    public void deleteMask(Bitmap bitmap) {
        this.maskController.deleteMask(bitmap);
    }

    public Bitmap deleteMask(float f, float f2, float f3) {
        this.xAtual = f;
        this.yAtual = f2;
        return this.maskController.deleteMask(f, f2, f3);
    }

    public Bitmap deletePointsMasking(float f, float f2, float f3) {
        this.xAtual = f;
        this.yAtual = f2;
        return this.maskController.delete_only_points_masking(f, f2, f3);
    }

    public Bitmap deleteMask_zoom(float f, float f2, float f3) {
        this.xAtual = f;
        this.yAtual = f2;
        return this.maskController.deleteMaskZoom(f, f2, f3);
    }

    public void addMask(Bitmap bitmap) {
        this.maskController.addMask(bitmap);
    }

    public Bitmap addMask(float f, float f2, float f3) {
        this.xAtual = f;
        this.yAtual = f2;
        return this.maskController.addMask(f, f2, f3);
    }

    public void to_paint(Bitmap bitmap, float f) {
        this.maskController.pintarCor(bitmap);
        Canvas canvas = new Canvas(bitmap);
        float f2 = STROKE_INIT;
        float max = Math.max(f2 / f, f2 / 2.0f);
        if (!(this.initial_selection == null || this.final_selection == null || !this.is_Selecting)) {
            this.paintSelect.setPathEffect(new DashPathEffect(new float[]{TriangleBitmap.STROKE_DOT_SPACE_INIT / f, (TriangleBitmap.STROKE_DOT_SPACE_INIT * 2.0f) / f}, 0.0f));
            this.paintSelect.setStrokeWidth(max);
            canvas.drawRect(this.initial_selection.getXInit(), this.initial_selection.getYInit(), this.final_selection.getXInit(), this.final_selection.getYInit(), this.paintSelect);
        }
        if (this.is_Masking) {
            this.masking_paint.setStrokeWidth(max);
            canvas.drawCircle(this.xAtual, this.yAtual, ((float) get_Ray_Mask()) / f, this.masking_paint);
        }
    }

    public void set_Initial_Mask(Bitmap bitmap) {
        if (bitmap == null) {
            this.maskController.clearMask();
        } else {
            this.maskController.setMask(bitmap);
        }
    }

    public static Bitmap getMask() {
        ToolsController lPMLWToolsController = tools_controller;
        if (lPMLWToolsController != null) {
            return lPMLWToolsController.maskController.mask;
        }
        return null;
    }

    public int get_Tam_Movement_Sequence() {
        return this.tam_Movement_Sequence;
    }

    public static int get_Ray_Mask() {
        return tools_controller.ray_Mask;
    }

    public int get_Tool_type() {
        return this.type_tool;
    }

    public boolean isPlayingPreview() {
        return this.playingPreview;
    }

    public void stopPreview() {
        this.playingPreview = false;
        ToolsListener toolsListener = this.toolsListener;
        if (toolsListener != null) {
            toolsListener.onStopPreview();
        }
    }

    public void closePreview() {
        this.playingPreview = false;
        relative_layout_speed_settings.setVisibility(View.GONE);
        relative_layout_brush_size.setVisibility(View.VISIBLE);
        Exportactivity.relative_layout_bottom_toolbar_functionalities.setVisibility(View.VISIBLE);
        ToolsListener toolsListener = this.toolsListener;
        if (toolsListener != null) {
            toolsListener.onStopPreview();
        }
    }

    public void playPreview() {
        this.playingPreview = true;
        relative_layout_speed_settings.setVisibility(View.VISIBLE);
        relative_layout_brush_size.setVisibility(View.GONE);
        ToolsListener toolsListener = this.toolsListener;
        if (toolsListener != null) {
            toolsListener.onPlayPreview();
        }
    }

    public boolean is_Erasing_Mask() {
        return this.is_Erasing_Mask;
    }

    public void set_relative_layout_brush_size_visibility(boolean z) {
        relative_layout_brush_size.setVisibility(z ? View.VISIBLE : View.GONE);
    }

    public void setSubToolVelocidadeVisibility(boolean z) {
        relative_layout_speed_settings.setVisibility(z ? View.VISIBLE : View.GONE);
    }

    public void set_eraseroptions_visibility(boolean z) {
        this.ll_eraser_options.setVisibility(z ? View.VISIBLE : View.GONE);
    }

    public void setDelete_visibility(boolean z) {
        if (z) {
            this.ll_delete_image.setVisibility(View.VISIBLE);
            this.btDelete.setVisibility(View.VISIBLE);
        }
    }

    public Bitmap get_set_Representation() {
        Bitmap createBitmap = Bitmap.createBitmap(this.tam_Movement_Sequence + 10, 10, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Point lPMLWPoint = new Point(5.0f, 5.0f, (float) (this.tam_Movement_Sequence + 5), 5.0f);
        lPMLWPoint.to_draw_only_point(canvas, 255, 1.0f);
        lPMLWPoint.to_draw_Arrow(canvas, 255, 1.0f);
        return createBitmap;
    }

    public Bitmap get_Radius_Representation() {
        Bitmap createBitmap = Bitmap.createBitmap(Math.round(((float) (get_Ray_Mask() * 10)) + (STROKE_INIT * 10.0f)), Math.round(((float) (get_Ray_Mask() * 10)) + (STROKE_INIT * 10.0f)), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        this.masking_paint.setStrokeWidth(STROKE_INIT);
        if (this.type_tool == 3) {
            this.masking_paint_representation.setColor(-65536);
        } else {
            this.masking_paint_representation.setColor(-16711936);
        }
        canvas.drawCircle((float) Math.round(((float) createBitmap.getWidth()) / 2.0f), (float) Math.round(((float) createBitmap.getHeight()) / 2.0f), ((float) get_Ray_Mask()) * 2.0f, this.masking_paint_representation);
        canvas.drawCircle((float) Math.round(((float) createBitmap.getWidth()) / 2.0f), (float) Math.round(((float) createBitmap.getHeight()) / 2.0f), ((float) get_Ray_Mask()) * 2.0f, this.masking_paint);
        return createBitmap;
    }

    public void set_Time_Preview(int i) {
        this.tempoPreview = i;
        this.seekbar_Time_Speed_setting.setProgress(1);
        this.seekbar_Time_Speed_setting.setProgress(2);
        this.seekbar_Time_Speed_setting.setProgress(10000 - i);
    }

    public static int getTempoPreview() {
        ToolsController lPMLWToolsController = tools_controller;
        if (lPMLWToolsController != null) {
            return lPMLWToolsController.tempoPreview;
        }
        return 10000;
    }

    public void setAlphaMask(int i) {
        this.alphaMask = i;
    }

    public static int getAlphaMask() {
        ToolsController lPMLWToolsController = tools_controller;
        return lPMLWToolsController == null ? INIT_ALPHA_MASK : lPMLWToolsController.alphaMask;
    }

    public static int getColorMask() {
        ToolsController lPMLWToolsController = tools_controller;
        if (lPMLWToolsController == null) {
            return -65536;
        }
        return lPMLWToolsController.maskController.getColor();
    }

    public void setToolsListener(ToolsListener toolsListener) {
        this.toolsListener = toolsListener;
    }

    public void setEnabled(boolean z) {
        if (z && !this.enabled) {
            this.playingPreview = false;
            relative_layout_brush_size.setVisibility(View.GONE);
            relative_layout_speed_settings.setVisibility(View.GONE);
            this.btDelete.setVisibility(View.GONE);
        }
        this.enabled = z;
        this.btn_Stabilize_custom_points.setEnabled(z);
        this.btn_movement_single_array.setEnabled(z);
        this.btMovSequencia.setEnabled(z);
        this.btMask.setEnabled(z);
        this.btn_Erase_Masking.setEnabled(z);
        this.btn_select_remove_points.setEnabled(z);
        this.btZoom.setEnabled(z);
    }
}
