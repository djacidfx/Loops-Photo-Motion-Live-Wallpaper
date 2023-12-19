package com.demo.livwllpaper.models;

import java.io.File;
import java.util.Date;


public class VideoFileModel {
    private File file;
    private String file_name;
    public Date lastModDate;
    private long last_modify;
    private String path;
    public String duration_final = this.duration_final;

    public String getDuration_final() {
        return this.duration_final;
    }

    public void setDuration_final(String str) {
        this.duration_final = str;
    }

    public VideoFileModel(File file, Date date) {
        this.file = file;
        this.file_name = file.getName();
        this.last_modify = this.file.lastModified();
        this.path = this.file.getPath();
        this.lastModDate = date;
    }

    public String getFile_name() {
        return this.file_name;
    }

    public void setFile_name(String str) {
        this.file_name = str;
    }

    public Date getLast_modify() {
        return this.lastModDate;
    }

    public void setLast_modify(long j) {
        this.last_modify = j;
    }

    public File getFile() {
        return this.file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getPath() {
        return this.path;
    }
}
