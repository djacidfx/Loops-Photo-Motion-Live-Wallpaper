package com.demo.livwllpaper.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.VideoView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.demo.livwllpaper.R;
import com.demo.livwllpaper.Interfaces.WallpapersClicked;

import java.util.ArrayList;


public class WallpaperAdapter extends RecyclerView.Adapter<WallpaperAdapter.ViewHolder> {
    WallpapersClicked LPMLW_wallpapers_clicked;
    boolean from_background;
    int height;
    LayoutInflater inflater;
    private ArrayList<String> list;
    private Context mContext;
    int selected_position = -1;
    SharedPreferences sharedDefault;
    SharedPreferences sharedUser;
    String title;
    int width;

    @Override 
    public long getItemId(int i) {
        return (long) i;
    }

    @Override 
    public int getItemViewType(int i) {
        return i;
    }

    public WallpaperAdapter(Context context, ArrayList<String> arrayList, WallpapersClicked lPMLWWallpapersClicked, int i, int i2) {
        this.mContext = context;
        this.list = arrayList;
        this.LPMLW_wallpapers_clicked = lPMLWWallpapersClicked;
        this.width = i;
        this.height = i2;
    }

    @Override 
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.picker_grid_item_gallery_thumbnail, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        final String str = "file:///android_asset/" + this.list.get(i);
        if (i == 0) {
            viewHolder.fl_sub.setLayoutParams(new FrameLayout.LayoutParams((int) (((double) this.width) / 2.7d), (int) (((double) this.height) / 1.04d)));
            viewHolder.cv_load.setVisibility(View.GONE);
            viewHolder.image_first.setVisibility(View.VISIBLE);
            Glide.with(this.mContext).load(Uri.parse(str)).into(viewHolder.image_first);
        } else {
            viewHolder.fl_sub.setLayoutParams(new FrameLayout.LayoutParams((int) (((double) this.width) / 2.9d), this.height / 1));
            viewHolder.cv_load.setVisibility(View.VISIBLE);
            viewHolder.image_first.setVisibility(View.GONE);
            Glide.with(this.mContext).load(Uri.parse(str)).into(viewHolder.mThumbnail);
        }
        viewHolder.fl_sub.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                WallpaperAdapter.this.LPMLW_wallpapers_clicked.on_clicked_position(i, str);
            }
        });
    }

    @Override 
    public int getItemCount() {
        return this.list.size();
    }

    
    
    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv_load;
        FrameLayout fl_sub;
        ImageView image_first;
        ImageView mThumbnail;
        FrameLayout root;
        VideoView video;

        public ViewHolder(View view) {
            super(view);
            this.cv_load = (CardView) view.findViewById(R.id.cv_load);
            this.image_first = (ImageView) view.findViewById(R.id.image_first);
            this.root = (FrameLayout) view.findViewById(R.id.root);
            this.mThumbnail = (ImageView) view.findViewById(R.id.thumbnail_image);
            this.fl_sub = (FrameLayout) view.findViewById(R.id.fl_sub);
        }
    }
}
