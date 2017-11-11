package com.ahmed.naqalati_driver.model;

/**
 * Created by bassiouny on 10/11/17.
 */

public class User {

    private String userName;
    private String userAvatar;
    private String userPhone;
    private String userPasswrod;
    private Double lat;
    private Double lng;
    private String currentRequest;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserPasswrod() {
        return userPasswrod;
    }

    public void setUserPasswrod(String userPasswrod) {
        this.userPasswrod = userPasswrod;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getCurrentRequest() {
        return currentRequest;
    }

    public void setCurrentRequest(String currentRequest) {
        this.currentRequest = currentRequest;
    }
}
