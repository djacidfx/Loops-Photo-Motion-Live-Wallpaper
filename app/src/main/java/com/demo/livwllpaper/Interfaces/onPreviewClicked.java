package com.demo.livwllpaper.Interfaces;

import com.demo.livwllpaper.beans.Project;
import java.util.List;


public interface onPreviewClicked {
    void on_item_clicked(String str, int i);

    void on_item_clicked(String str, List<Project> list, int i);
}
