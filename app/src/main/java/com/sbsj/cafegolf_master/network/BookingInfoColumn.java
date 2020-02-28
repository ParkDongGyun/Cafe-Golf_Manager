package com.sbsj.cafegolf_master.network;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BookingInfoColumn implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("member_id")
    private int member_id;
    @SerializedName("book_date")
    private String book_date;
    @SerializedName("isapproved")
    private int isapproved;
    @SerializedName("member_name")
    private String member_name;
    @SerializedName("fb_token")
    private String fb_token;

    public int getId() {
        return id;
    }

    public int getMember_id() {
        return member_id;
    }

    public String getBook_date() {
        return book_date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public void setBook_date(String book_date) {
        this.book_date = book_date;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public void setIsapproved(int isapproved) {
        this.isapproved = isapproved;
    }

    public int getIsapproved() {
        return isapproved;
    }

    public String getFb_token() {
        return fb_token;
    }

    public void setFb_token(String fb_token) {
        this.fb_token = fb_token;
    }
}
