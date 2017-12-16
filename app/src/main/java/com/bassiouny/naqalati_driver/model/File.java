package com.bassiouny.naqalati_driver.model;

/**
 * Created by bassiouny on 16/12/17.
 */

public class File {
    private String userUploadedId;
    private String path;
    private String userType;
    private String date;


    public String getUserUploadedId() {
        return userUploadedId;
    }

    public void setUserUploadedId(String userUploadedId) {
        this.userUploadedId = userUploadedId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

