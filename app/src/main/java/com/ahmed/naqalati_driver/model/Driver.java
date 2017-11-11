package com.ahmed.naqalati_driver.model;

/**
 * Created by bassiouny on 11/11/17.
 */

public class Driver extends User {

    private CarType carType;
    private String carNumber;

    public CarType getCarType() {
        return carType;
    }

    public void setCarType(CarType carType) {
        this.carType = carType;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }
}
