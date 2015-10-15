package com.mhanyamoto.mhanyamoto.service;



import java.util.List;
import com.mhanyamoto.mhanyamoto.entity.Bid;
import com.mhanyamoto.mhanyamoto.entity.Login;
import com.mhanyamoto.mhanyamoto.entity.Payment;
import com.mhanyamoto.mhanyamoto.entity.Vehicles;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.mime.TypedFile;

/**
 * Created by 10 on 9/25/2015.
 */
public interface Service {



    @Multipart
    @POST("/registerAPI")
    void register(@Part("username") String username, @Part("email") String email, @Part("password") String password, @Part("phone") String phone, @Part("address") String address, @Part("vehicles") String vehicles, @Part("charge") String charge, @Part("payment") String payment,@Part("time_start") String time_start ,@Part("time_end") String time_end , @Part("longitude") String longitude, @Part("latitude") String latitude, @Part("photo") TypedFile photo,  Callback<Login> callback);

    @FormUrlEncoded
    @POST("/loginAPI")
    void login(@Field("email") String email, @Field("password") String password, Callback<Login> callback);

    @GET("/getBids")
    void getBids(Callback <List<Bid>> callback);

    @GET("/getOrder/{idUser}")
    void getOrder(@Path("idUser") String idUser, Callback <List<Bid>> callback);

    @Multipart
    @POST("/photo")
    void upload(@Part("photo") TypedFile photo,Callback<String> cb);

    @GET("/getPayments")
    void getPayments(Callback <List<Payment>> callback);

    @GET("/getVehicles")
    void getVehicles(Callback <List<Vehicles>> callback);

    @FormUrlEncoded
    @POST("/setBid")
    void setBid(@Field("id_user") String id_user, @Field("id_request") String id_request, @Field("price") String price, Callback<String> callback);

}
