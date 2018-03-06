package com.bassiouny.naqalati_driver.model;

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
    private String numberID;
    private String address;
    private String email;
    private String token;
    private Boolean isOnline;


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
    	if(lat==null)
    		lat=0.0;
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
    	if(lng==null)
    		lng=0.0;
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getCurrentRequest() {
    	if(currentRequest==null)
    		currentRequest="";
        return currentRequest;
    }

    public void setCurrentRequest(String currentRequest) {
        this.currentRequest = currentRequest;
    }
    public String getNumberID() {
        if(numberID==null)
            numberID="";
        return numberID;
    }

    public void setNumberID(String numberID) {
        this.numberID = numberID;
    }

    public String getAddress() {
        if(address==null)
            address="";
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getEmail() {
        if(email==null)
            email="";
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        if(token==null)
            token="";
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isOnline() {
        if(isOnline == null)
            return false;
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
}
