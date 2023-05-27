/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.app.entity;

public class UserResponse {

    public static final String RESPONSE_CODE_VALID = "0";
    public static final String RESPONSE_CODE_INVALID = "1";
    public static final String RESPONSE_CODE_INTERNAL_ERROR = "999";

    private String responseCode;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }
}
