package com.mhanyamoto.mhanyamoto.entity;

/**
 * Created by 10 on 9/25/2015.
 */
public class Bid {
    private String id;
    private String id_user;
    private String title;
    private String open_date;
    private String close_date;
    private String open_price;
    private String longitude_from;
    private String latitude_from;
    private String desc_from;
    private String longitude_to;
    private String latitude_to;
    private String desc_to;
    private String distance;
    private String duration;
    private String status;
    private String expire;
    private String created_at;
    private String updated_at;
    private String current_bid;
    private String image;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getClose_date() {
        return close_date;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getDesc_from() {
        return desc_from;
    }

    public String getDesc_to() {
        return desc_to;
    }

    public String getDistance() {
        return distance;
    }

    public String getDuration() {
        return duration;
    }

    public String getExpire() {
        return expire;
    }

    public String getId_user() {
        return id_user;
    }

    public String getLatitude_from() {
        return latitude_from;
    }

    public String getLatitude_to() {
        return latitude_to;
    }

    public String getLongitude_from() {
        return longitude_from;
    }

    public String getLongitude_to() {
        return longitude_to;
    }

    public String getOpen_date() {
        return open_date;
    }

    public String getOpen_price() {
        return open_price;
    }

    public String getStatus() {
        return status;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getCurrent_bid() {
        return current_bid;
    }

    public String getImage() {
        return image;
    }
}
