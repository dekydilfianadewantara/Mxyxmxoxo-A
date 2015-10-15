package com.mhanyamoto.mhanyamoto.entity;

/**
 * Created by 10 on 10/13/2015.
 */
public class Vehicles {
    private String id;
    private String name;

    public Vehicles (String id , String name){
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
