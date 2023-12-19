package com.demo.livwllpaper.adapters;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.demo.livwllpaper.R;
import com.demo.livwllpaper.Interfaces.onPreviewClicked;
import com.demo.livwllpaper.activities.CreationActivity;

import java.util.ArrayList;

public class Whomescreenadapter extends RecyclerView.Adapter<Whomescreenadapter.ViewHolder> {
    public static ArrayList<String> delete_video_list = new ArrayList<>();

    onPreviewClicked LPMLWon_preview_clicked;
    int height;
    private LayoutInflater inflater;
    private Context mContext;
    private ArrayList<String> mStringArrayList;
    private FrameLayout.LayoutParams params;
    private ArrayList<String> tempStringArrayList = new ArrayList<>();
    int width;

    public Whomescreenadapter(Context context, ArrayList<String> arrayList, int i, int i2, onPreviewClicked lPMLWonPreviewClicked) {
        this.mContext = context;
        this.mStringArrayList = arrayList;
        this.width = i;
        this.height = i2;
        this.LPMLWon_preview_clicked = lPMLWonPreviewClicked;
    }

    @Override 
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gallery_item, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        viewHolder.cv_main.setLayoutParams(new LinearLayout.LayoutParams((int) (((double) this.width) / 2.9d), this.height / 2));
        final String str = this.mStringArrayList.get(i);
        RequestBuilder<Drawable> load = Glide.with(this.mContext).load(str);
        int i2 = this.width;
        load.override(i2 / 3, i2 / 3).into(viewHolder.ivAlbumThumb);
        viewHolder.ivAlbumThumb.setScaleType(ImageView.ScaleType.CENTER_CROP);
        viewHolder.fl_main.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                if (Whomescreenadapter.this.LPMLWon_preview_clicked != null) {
                    Whomescreenadapter.this.LPMLWon_preview_clicked.on_item_clicked(str, i);
                }
            }
        });
    }

    public void select_all() {
        ArrayList<String> arrayList = this.mStringArrayList;
        if (!(arrayList == null || arrayList.size() == 0)) {
            ArrayList<String> arrayList2 = delete_video_list;
            if (!(arrayList2 == null || arrayList2.size() == 0)) {
                delete_video_list.clear();
            }
            for (int i = 0; i < this.mStringArrayList.size(); i++) {
                delete_video_list.add(this.mStringArrayList.get(i));
            }
            TextView textView = CreationActivity.tv_count;
            textView.setText(" ( " + delete_video_list.size() + " )");
            notifyDataSetChanged();
        }
    }

    public void unselect_all() {
        ArrayList<String> arrayList = delete_video_list;
        if (!(arrayList == null || arrayList.size() == 0)) {
            delete_video_list.clear();
        }
        TextView textView = CreationActivity.tv_count;
        textView.setText(" ( " + delete_video_list.size() + " )");
        notifyDataSetChanged();
    }

    @Override 
    public int getItemCount() {
        if (this.mStringArrayList.size() > 10) {
            return 10;
        }
        return this.mStringArrayList.size();
    }

    public ArrayList<String> getData() {
        return this.tempStringArrayList;
    }

    
    public class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox cbselect;
        LinearLayout cv_main;
        FrameLayout fl_main;
        private ImageView ivAlbumThumb;

        public ViewHolder(View view) {
            super(view);
            this.ivAlbumThumb = (ImageView) view.findViewById(R.id.ivAlbumThumb);
            this.cbselect = (CheckBox) view.findViewById(R.id.cbselect);
            this.cv_main = (LinearLayout) view.findViewById(R.id.cv_main);
            this.fl_main = (FrameLayout) view.findViewById(R.id.fl_main);
        }
    }
}
