package com.sample.app.common.security;

public class WebServiceUser {
    private final String userId;
    private  final  String userName;

    public WebServiceUser(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }
}
