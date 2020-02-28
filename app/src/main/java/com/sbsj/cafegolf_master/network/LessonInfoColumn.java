package com.sbsj.cafegolf_master.network;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LessonInfoColumn implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("hour_before")
    private int hour_before;
    @SerializedName("minute_before")
    private int minute_before;

    public LessonInfoColumn(int hour_before, int minute_before) {
        this.hour_before = hour_before;
        this.minute_before = minute_before;
    }

    public int getId() {
        return id;
    }

    public int getHour_before() {
        return hour_before;
    }

    public int getMinute_before() {
        return minute_before;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setHour_before(int hour_before) {
        this.hour_before = hour_before;
    }

    public void setMinute_before(int minute_before) {
        this.minute_before = minute_before;
    }
}