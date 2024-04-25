package com.roommake.user.security;

public abstract class LoginUser {
    private int id;

    public LoginUser(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}