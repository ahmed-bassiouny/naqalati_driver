package com.bassiouny.naqalati_driver.model;

/**
 * Created by bassiouny on 11/11/17.
 */

public class Driver extends User {

    private String carType;
    private String carNumber;
    private String edaraMeror;
    private String we7detMeror;
    private String ro5esa;
    private String ro5esaNumber;
    private boolean adminAccept;
    private String codeAgent;

    // car
    private String carOwner;
    private String phoneOwner;
    private String city;
    private String we7datMerorOwner;
    private String shaceh;
    private String motor;
    private String size;
    private String model;

    public String getCarType() {
        if(carType==null)
            carType="";
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

    public String getEdaraMeror() {
        return edaraMeror;
    }

    public void setEdaraMeror(String edaraMeror) {
        this.edaraMeror = edaraMeror;
    }

    public String getWe7detMeror() {
        return we7detMeror;
    }

    public void setWe7detMeror(String we7detMeror) {
        this.we7detMeror = we7detMeror;
    }

    public String getRo5esa() {
        return ro5esa;
    }

    public void setRo5esa(String ro5esa) {
        this.ro5esa = ro5esa;
    }

    public String getRo5esaNumber() {
        return ro5esaNumber;
    }

    public void setRo5esaNumber(String ro5esaNumber) {
        this.ro5esaNumber = ro5esaNumber;
    }

    public String getCarOwner() {
        return carOwner;
    }

    public void setCarOwner(String carOwner) {
        this.carOwner = carOwner;
    }

    public String getPhoneOwner() {
        return phoneOwner;
    }

    public void setPhoneOwner(String phoneOwner) {
        this.phoneOwner = phoneOwner;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWe7datMerorOwner() {
        return we7datMerorOwner;
    }

    public void setWe7datMerorOwner(String we7datMerorOwner) {
        this.we7datMerorOwner = we7datMerorOwner;
    }

    public String getShaceh() {
        return shaceh;
    }

    public void setShaceh(String shaceh) {
        this.shaceh = shaceh;
    }

    public String getMotor() {
        return motor;
    }

    public void setMotor(String motor) {
        this.motor = motor;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public boolean isAdminAccept() {
        return adminAccept;
    }

    public void setAdminAccept(boolean adminAccept) {
        this.adminAccept = adminAccept;
    }

    public String getCodeAgent() {
        return codeAgent;
    }

    public void setCodeAgent(String codeAgent) {
        this.codeAgent = codeAgent;
    }
}
