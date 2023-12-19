package com.demo.livwllpaper.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.demo.livwllpaper.R;
import com.demo.livwllpaper.Utilsx.Utils;
import com.demo.livwllpaper.fragments.TutorialFragment;
import com.demo.livwllpaper.views.TooltipIndicator;


public class TutorialActivity extends AppCompatActivity {
    static int position_static;
    public TooltipIndicator indicator;
    TextView tv_header;
    TextView tv_skip;
    TextView tv_sub_header;
    ViewPager view_pager;

    @Override 
    public void onBackPressed() {
    }

    
    @Override 
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_tutorial);
        this.view_pager = (ViewPager) findViewById(R.id.view_pager);
        this.tv_skip = (TextView) findViewById(R.id.tv_skip);
        this.indicator = (TooltipIndicator) findViewById(R.id.tooltip_indicator);
        this.tv_header = (TextView) findViewById(R.id.tv_header);
        this.tv_sub_header = (TextView) findViewById(R.id.tv_sub_header);
        this.view_pager.setOffscreenPageLimit(0);
        setupViewPager(this.view_pager);
        this.indicator.setupViewPager(this.view_pager);
        this.tv_skip.setText("Skip");
        this.tv_header.setText(getResources().getString(R.string.Tutorial_page_two_header));
        this.tv_sub_header.setText(getResources().getString(R.string.Tutorial_page_two_sub_header));
        this.tv_skip.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                SharedPreferences.Editor edit = TutorialActivity.this.getSharedPreferences(Utils.MY_PREFS_NAME, 0).edit();
                edit.putBoolean("tutorial_seen", true);
                edit.apply();
                TutorialActivity.this.startActivity(new Intent(TutorialActivity.this, HomeActivity.class));
                TutorialActivity.this.finish();
            }
        });
        this.view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() { 
            @Override 
            public void onPageScrollStateChanged(int i) {
            }

            @Override 
            public void onPageScrolled(int i, float f, int i2) {
            }

            @Override 
            public void onPageSelected(int i) {
                TutorialActivity.position_static = i;
                if (i == 0) {
                    TutorialActivity.this.tv_header.setText(TutorialActivity.this.getResources().getString(R.string.Tutorial_page_two_header));
                    TutorialActivity.this.tv_sub_header.setText(TutorialActivity.this.getResources().getString(R.string.Tutorial_page_two_sub_header));
                    TutorialActivity.this.tv_skip.setText("Skip");
                } else if (i == 1) {
                    TutorialActivity.this.tv_header.setText(TutorialActivity.this.getResources().getString(R.string.Tutorial_page_three_header));
                    TutorialActivity.this.tv_sub_header.setText(TutorialActivity.this.getResources().getString(R.string.Tutorial_page_three_sub_header));
                    TutorialActivity.this.tv_skip.setText("Skip");
                } else if (i == 2) {
                    TutorialActivity.this.tv_header.setText(TutorialActivity.this.getResources().getString(R.string.Tutorial_page_four_header));
                    TutorialActivity.this.tv_sub_header.setText(TutorialActivity.this.getResources().getString(R.string.Tutorial_page_four_sub_header));
                    TutorialActivity.this.tv_skip.setText("Skip");
                } else if (i == 3) {
                    TutorialActivity.this.tv_header.setText(TutorialActivity.this.getResources().getString(R.string.Tutorial_page_six_header));
                    TutorialActivity.this.tv_sub_header.setText(TutorialActivity.this.getResources().getString(R.string.Tutorial_page_six_sub_header));
                    TutorialActivity.this.tv_skip.setText("Skip");
                } else if (i == 4) {
                    TutorialActivity.this.tv_header.setText(TutorialActivity.this.getResources().getString(R.string.Tutorial_page_seven_header));
                    TutorialActivity.this.tv_sub_header.setText(TutorialActivity.this.getResources().getString(R.string.Tutorial_page_seven_footer_point_two));
                    TutorialActivity.this.tv_skip.setText("Done");
                }
            }
        });
    }

    private int getItem(int i) {
        return this.view_pager.getCurrentItem() + i;
    }

    public void setupViewPager(ViewPager viewPager) {
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
    }

    
    public class ViewPagerAdapter extends FragmentPagerAdapter {
        @Override 
        public int getCount() {
            return 5;
        }

        @Override 
        public int getItemPosition(Object obj) {
            return -2;
        }

        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override 
        public Fragment getItem(int i) {
            return TutorialFragment.newInstance(i);
        }
    }
}
