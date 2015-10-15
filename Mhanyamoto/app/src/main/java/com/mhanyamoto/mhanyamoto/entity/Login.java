package com.mhanyamoto.mhanyamoto.entity;

/**
 * Created by DDD on 10/10/2015.
 */
public class Login {
    private boolean success;
    private String id;
    private String email;
    private String name;

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public boolean isSuccess() {
        return success;
    }
}
