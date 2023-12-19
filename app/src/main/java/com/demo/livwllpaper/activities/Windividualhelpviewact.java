package com.demo.livwllpaper.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.demo.livwllpaper.R;
public class Windividualhelpviewact extends AppCompatActivity {
    ImageView btn_close_tutorial;
    CardView cv_for_imageview;
    ImageView iv_effect_image;
    ImageView iv_frame_on_tutorial_image;
    ImageView iv_logo_image;
    ImageView iv_tutorial_image;
    int position_in_fragment;
    TextView tv_footer_point_four;
    TextView tv_footer_point_one;
    TextView tv_footer_point_three;
    TextView tv_footer_point_two;
    TextView tv_header;
    TextView tv_sub_header;

    
    @Override 
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activityindividualscreen);
        getIntent().getIntExtra("pos", 0);
        this.tv_header = (TextView) findViewById(R.id.tv_header);
        this.tv_sub_header = (TextView) findViewById(R.id.tv_sub_header);
        this.tv_footer_point_one = (TextView) findViewById(R.id.tv_footer_point_one);
        this.tv_footer_point_two = (TextView) findViewById(R.id.tv_footer_point_two);
        this.tv_footer_point_three = (TextView) findViewById(R.id.tv_footer_point_three);
        this.cv_for_imageview = (CardView) findViewById(R.id.cv_for_imageview);
        this.btn_close_tutorial = (ImageView) findViewById(R.id.btn_close_tutorial);
        this.iv_effect_image = (ImageView) findViewById(R.id.iv_effect_image);
        this.iv_tutorial_image = (ImageView) findViewById(R.id.iv_tutorial_image);
        this.iv_frame_on_tutorial_image = (ImageView) findViewById(R.id.iv_frame_on_tutorial_image);
        this.iv_logo_image = (ImageView) findViewById(R.id.iv_logo_image);
        this.btn_close_tutorial.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                Windividualhelpviewact.this.onBackPressed();
            }
        });
    }

    @Override 
    public void onBackPressed() {
        super.onBackPressed();
    }
}
