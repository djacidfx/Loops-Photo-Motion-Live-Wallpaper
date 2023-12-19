package com.demo.livwllpaper.adapters;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.demo.livwllpaper.fragments.MyCreationFragment;
import com.demo.livwllpaper.fragments.TemplateFragment;

public class MyPagerAdapter extends FragmentPagerAdapter {
    private String TAG = "MyPagerAdapter";
    private Context mContext;

    public MyPagerAdapter(@NonNull FragmentManager fm,String s) {
        super(fm);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new TemplateFragment();
            case 1:
                return new MyCreationFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 0;
    }
}
