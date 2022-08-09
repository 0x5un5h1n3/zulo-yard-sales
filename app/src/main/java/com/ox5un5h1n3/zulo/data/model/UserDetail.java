package com.ox5un5h1n3.zulo.data.model;

public class UserDetail {
    private String username;
    private String email;
    private String userUid;
    private String phoneNumber;
    private String address;

    public UserDetail() {}

    public UserDetail(String username, String email, String userUid, String phoneNumber, String address) {
        this.username = username;
        this.email = email;
        this.userUid = userUid;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
