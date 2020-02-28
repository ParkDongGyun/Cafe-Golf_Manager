package com.sbsj.cafegolf_master.network;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BookingInfo {
    @SerializedName("bookinglist")
    private ArrayList<BookingInfoColumn> bookinglist = new ArrayList<>();

    public ArrayList<BookingInfoColumn> getBookinglist() {
        return bookinglist;
    }
}