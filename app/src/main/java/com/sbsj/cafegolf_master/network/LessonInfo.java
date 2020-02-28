package com.sbsj.cafegolf_master.network;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LessonInfo {
    @SerializedName("lessonlist")
    private ArrayList<LessonInfoColumn> lessonlist = new ArrayList<>();

    public ArrayList<LessonInfoColumn> getLessonlist() {
        return lessonlist;
    }
}
