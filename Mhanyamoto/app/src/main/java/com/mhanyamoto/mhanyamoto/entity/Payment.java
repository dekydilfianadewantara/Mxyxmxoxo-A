package com.mhanyamoto.mhanyamoto.entity;

/**
 * Created by 10 on 9/25/2015.
 */
public class Payment {
    private String id;
    private String name;

    public Payment (String id , String name){
        this.id = id;
        this.name = name;
    }
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
