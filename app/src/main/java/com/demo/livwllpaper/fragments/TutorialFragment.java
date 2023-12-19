package com.demo.livwllpaper.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.demo.livwllpaper.R;

import java.util.concurrent.RejectedExecutionException;


public class TutorialFragment extends Fragment {
    ImageView iv_tutorial_image;
    int position_in_fragment;
    public static TutorialFragment newInstance(int i) {
        TutorialFragment lPMLWTutorialFragment = new TutorialFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position_inside", i);
        lPMLWTutorialFragment.setArguments(bundle);
        return lPMLWTutorialFragment;
    }
    @Override 
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.position_in_fragment = getArguments().getInt("position_inside");
    }
    @Override 
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_tutorial_start, viewGroup, false);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.iv_tutorial_image);
        this.iv_tutorial_image = imageView;
        imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { 
            @Override 
            public void onGlobalLayout() {
                if (TutorialFragment.this.iv_tutorial_image.getMeasuredWidth() > 0) {
                    TutorialFragment.this.iv_tutorial_image.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    TutorialFragment.this.iv_tutorial_image.getMeasuredWidth();
                    TutorialFragment.this.iv_tutorial_image.getMeasuredHeight();
                    int i = TutorialFragment.this.position_in_fragment;
                    try {
                        if (i == 0) {
                            Glide.with(TutorialFragment.this.getContext()).load(Integer.valueOf((int) R.drawable.tutorial_image_with_motion_effect)).into(TutorialFragment.this.iv_tutorial_image);
                        } else if (i == 1) {
                            Glide.with(TutorialFragment.this.getContext()).load(Integer.valueOf((int) R.drawable.tutorial_image_with_sequence_motion_effect)).into(TutorialFragment.this.iv_tutorial_image);
                        } else if (i == 2) {
                            Glide.with(TutorialFragment.this.getContext()).load(Integer.valueOf((int) R.drawable.tutorial_image_with_stabilisation_effect)).into(TutorialFragment.this.iv_tutorial_image);
                        } else if (i != 3) {
                            if (i == 4) {
                                Glide.with(TutorialFragment.this.getContext()).load(Integer.valueOf((int) R.drawable.tutorial_image_with_erase_effect)).into(TutorialFragment.this.iv_tutorial_image);
                            }
                        } else {
                            Glide.with(TutorialFragment.this.getContext()).load(Integer.valueOf((int) R.drawable.tutorial_image_with_masking_effect)).into(TutorialFragment.this.iv_tutorial_image);
                        }
                    } catch (RejectedExecutionException unused) {
                    }
                }
            }
        });
        return inflate;
    }
}
