package com.sbsj.cafegolf_master.network;

import com.google.gson.annotations.SerializedName;

public class MasterInfoColumn {
    @SerializedName("id")
    private int id;
    @SerializedName("fb_token")
    private String fb_token;

    public int getId() {
        return id;
    }

    public String getFb_token() {
        return fb_token;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFb_token(String fb_token) {
        this.fb_token = fb_token;
    }
}
