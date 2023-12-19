package com.demo.livwllpaper.adapters;

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


public class Creationadapter extends RecyclerView.Adapter<Creationadapter.ViewHolder> {
    public static ArrayList<String> delete_video_list = new ArrayList<>();
    onPreviewClicked LPMLWon_preview_clicked;
    private LayoutInflater inflater;
    private Context mContext;
    private ArrayList<String> mStringArrayList;
    private FrameLayout.LayoutParams params;
    private ArrayList<String> tempStringArrayList = new ArrayList<>();
    int width;

    public Creationadapter(Context context, ArrayList<String> arrayList, int i, onPreviewClicked lPMLWonPreviewClicked) {
        this.mContext = context;
        this.mStringArrayList = arrayList;
        this.width = i;
        this.LPMLWon_preview_clicked = lPMLWonPreviewClicked;
    }

    @Override 
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mycreation_item, viewGroup, false));
    }

    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        LinearLayout linearLayout = viewHolder.cv_main;
        int i2 = this.width;
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams((int) (((double) i2) / 2.9d), (int) (((double) i2) / 2.15d)));
        final String str = this.mStringArrayList.get(i);
        RequestBuilder<Drawable> load = Glide.with(this.mContext).load(str);
        int i3 = this.width;
        load.override(i3 / 3, i3 / 3).into(viewHolder.ivAlbumThumb);
        viewHolder.ivAlbumThumb.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ArrayList<String> arrayList = delete_video_list;
        if (arrayList != null) {
            if (arrayList.size() > 0) {
                viewHolder.cbselect.setVisibility(View.VISIBLE);
                if (delete_video_list.contains(str)) {
                    viewHolder.cbselect.setChecked(true);
                    viewHolder.cbselect.setButtonDrawable(R.drawable.iv_check_adapter);
                } else {
                    viewHolder.cbselect.setChecked(false);
                    viewHolder.cbselect.setButtonDrawable(R.drawable.iv_uncheck_adapter);
                }
            } else {
                viewHolder.cbselect.setVisibility(View.GONE);
            }
        }
        viewHolder.ivAlbumThumb.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                if (Creationadapter.delete_video_list != null) {
                    if (Creationadapter.delete_video_list.size() > 0) {
                        if (Creationadapter.delete_video_list.contains(str)) {
                            Creationadapter.delete_video_list.remove(str);
                            if (Creationadapter.delete_video_list.size() != Creationadapter.this.mStringArrayList.size()) {
                                CreationActivity.show_delete_icon();
                            }
                            viewHolder.cbselect.setVisibility(View.VISIBLE);
                            viewHolder.cbselect.setButtonDrawable(R.drawable.iv_uncheck_adapter);
                        } else {
                            Creationadapter.delete_video_list.add(str);
                            viewHolder.cbselect.setVisibility(View.VISIBLE);
                            viewHolder.cbselect.setButtonDrawable(R.drawable.iv_check_adapter);
                            Creationadapter.this.notifyDataSetChanged();
                        }
                        if (Creationadapter.delete_video_list.size() == 0) {
                            CreationActivity.hide_delete_icon();
                            Creationadapter.this.notifyDataSetChanged();
                            return;
                        }
                        if (Creationadapter.delete_video_list.size() == Creationadapter.this.mStringArrayList.size()) {
                            CreationActivity.set_selectall_icon();
                        } else {
                            CreationActivity.unset_selectall_icon();
                        }
                        CreationActivity.show_delete_icon();
                    } else if (Creationadapter.this.LPMLWon_preview_clicked != null) {
                        Creationadapter.this.LPMLWon_preview_clicked.on_item_clicked(str, i);
                    }
                } else if (Creationadapter.this.LPMLWon_preview_clicked != null) {
                    Creationadapter.this.LPMLWon_preview_clicked.on_item_clicked(str, i);
                }
            }
        });
        viewHolder.ivAlbumThumb.setOnLongClickListener(new View.OnLongClickListener() { 
            @Override 
            public boolean onLongClick(View view) {
                if (Creationadapter.delete_video_list == null) {
                    return true;
                }
                if (Creationadapter.delete_video_list.contains(str)) {
                    Creationadapter.delete_video_list.remove(str);
                    if (Creationadapter.delete_video_list.size() != Creationadapter.this.mStringArrayList.size()) {
                        CreationActivity.show_delete_icon();
                    }
                    viewHolder.cbselect.setVisibility(View.VISIBLE);
                    viewHolder.cbselect.setButtonDrawable(R.drawable.iv_uncheck_adapter);
                } else {
                    Creationadapter.delete_video_list.add(str);
                    viewHolder.cbselect.setVisibility(View.VISIBLE);
                    viewHolder.cbselect.setButtonDrawable(R.drawable.iv_check_adapter);
                    Creationadapter.this.notifyDataSetChanged();
                }
                if (Creationadapter.delete_video_list.size() == 0) {
                    CreationActivity.hide_delete_icon();
                    Creationadapter.this.notifyDataSetChanged();
                    return true;
                }
                if (Creationadapter.delete_video_list.size() == Creationadapter.this.mStringArrayList.size()) {
                    CreationActivity.set_selectall_icon();
                } else {
                    CreationActivity.unset_selectall_icon();
                }
                CreationActivity.show_delete_icon();
                return true;
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
        return this.mStringArrayList.size();
    }

    public ArrayList<String> getData() {
        return this.tempStringArrayList;
    }

    
    public class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox cbselect;
        LinearLayout cv_main;
        private ImageView ivAlbumThumb;

        public ViewHolder(View view) {
            super(view);
            this.ivAlbumThumb = (ImageView) view.findViewById(R.id.ivAlbumThumb);
            this.cbselect = (CheckBox) view.findViewById(R.id.cbselect);
            this.cv_main = (LinearLayout) view.findViewById(R.id.cv_main);
        }
    }
}
