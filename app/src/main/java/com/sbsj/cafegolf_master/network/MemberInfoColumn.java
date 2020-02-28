package com.sbsj.cafegolf_master.network;

import com.google.gson.annotations.SerializedName;

public class MemberInfoColumn {
    @SerializedName("id")
    private int id;
    @SerializedName("directory")
    private String directory;
    @SerializedName("sns_id")
    private String sns_id;
    @SerializedName("name")
    private String name;
    @SerializedName("phone")
    private String phone;
    @SerializedName("lesson_count")
    private int lesson_count;
    @SerializedName("date")
    private String date;
    @SerializedName("fb_token")
    private String fb_token;

    public void setId(int id) {
        this.id = id;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public void setSns_id(String sns_id) {
        this.sns_id = sns_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setLesson_count(int lesson_count) {
        this.lesson_count = lesson_count;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setFb_token(String fb_token) {
        this.fb_token = fb_token;
    }

    public int getId() {
        return id;
    }

    public String getDirectory() {
        return directory;
    }

    public String getSns_id() {
        return sns_id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public int getLesson_count() {
        return lesson_count;
    }

    public String getDate() {
        return date;
    }

    public String getFb_token() {
        return fb_token;
    }
}
