package com.bassiouny.naqalati_driver.model;

import java.io.Serializable;

/**
 * Created by bassiouny on 12/11/17.
 */

public class RequestInfo implements Serializable {
    // user
    private String userId;
    private String userName;
    private String userPhone;
    private String userImage;
    private Double userLat;
    private Double userLng;
    // driver
    private String driverId;
    private String driverName;
    private String driverImage;
    private String driverPhone;
    private Double driverLng;
    private Double driverLat;
    private String carType;
    private String carNumber;
    // request info
    private RequestStatus requestStatus;
    private String reason;
    private Point startPoint;
    private Point endPoint;

    public void setUserInfo(String userId,String userName ,String userPhone, String userImage,Double userLat,Double userLng){
        this.userId=userId;
        this.userName=userName;
        this.userPhone=userPhone;
        this.userImage=userImage;
        this.userLat=userLat;
        this.userLng=userLng;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public Double getUserLat() {
        return userLat;
    }

    public void setUserLat(Double userLat) {
        this.userLat = userLat;
    }

    public Double getUserLng() {
        return userLng;
    }

    public void setUserLng(Double userLng) {
        this.userLng = userLng;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverImage() {
        return driverImage;
    }

    public void setDriverImage(String driverImage) {
        this.driverImage = driverImage;
    }

    public Double getDriverLng() {
        return driverLng;
    }

    public void setDriverLng(Double driverLng) {
        this.driverLng = driverLng;
    }

    public Double getDriverLat() {
        return driverLat;
    }

    public void setDriverLat(Double driverLat) {
        this.driverLat = driverLat;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }
}
