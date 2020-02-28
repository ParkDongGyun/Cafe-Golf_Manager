package com.sbsj.cafegolf_master.network;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DB_Service {
    @GET("get_memberinfo.php")
    Call<MemberInfo> getmemberinfo();

    @FormUrlEncoded
    @POST("update_memberinfo.php")
    Call<String> updatememberinfo(@Field("id") int id, @Field("lesson_count") int lesson_count);

    @FormUrlEncoded
    @POST("delete_memberinfo.php")
    Call<String> deletememberinfo(@Field("id") int id);

    @GET("get_bookinginfo.php")
    Call<BookingInfo> getBookingInfo(@Query("date") String date);

    @FormUrlEncoded
    @POST("update_boookinginfo.php")
    Call<String> updatebookinginfo(@Field("id") int id, @Field("isapproved") int isapproved);

    @FormUrlEncoded
    @POST("delete_bookinginfo.php")
    Call<String> deletebookinginfo(@Field("id") int id);

    @GET("get_lessoninfo.php")
    Call<LessonInfo> getLessonInfo();

    @FormUrlEncoded
    @POST("insert_lessoninfo.php")
    Call<String> insertlessoninfo(@Field("hour_before[]") ArrayList<Integer> hour_before,
                                  @Field("minute_before[]") ArrayList<Integer> minute_before);

    @FormUrlEncoded
    @POST("update_lessoninfo.php")
    Call<String> updatelessoninfo(@Field("id[]") ArrayList<Integer> id,
                                  @Field("hour_before[]") ArrayList<Integer> hour_before,
                                  @Field("minute_before[]") ArrayList<Integer> minute_before);

    @FormUrlEncoded
    @POST("delete_lessoninfo.php")
    Call<String> deletelessoninfo(@Field("id") int id);

    @POST("get_masterinfo.php")
    Call<MasterInfo> get_masterinfo();

    @FormUrlEncoded
    @POST("insert_masterinfo.php")
    Call<String> insert_masterinfo(@Field("fb_token") String fb_token);
}
