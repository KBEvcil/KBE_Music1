package com.example.test;

import java.io.Serializable;

public class MusicListData implements Serializable {
    private String path;
    private String name;
    private String duration;

    public MusicListData(String path, String name, String duration) {
        this.path = path;
        this.name = name;
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
