package com.ichat4j.wrj.robot.tuling.entity;

public class UserInfo {
    private String apiKey;
    private String userId;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserInfo key(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    public UserInfo userId(String userId) {
        this.userId = userId;
        return this;
    }
}
