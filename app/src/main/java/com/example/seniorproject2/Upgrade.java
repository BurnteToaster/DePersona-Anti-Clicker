package com.example.seniorproject2;

import android.view.View;

public class Upgrade {
    private int id;
    private int power;
    private int price;
    private int count;
    private float powerCoefficient;
    private float priceCoefficient;

    public Upgrade(int id, int power, int price, int count, float powerCoefficient, float priceCoefficient) {
        this.id = id;
        this.power = power;
        this.price = price;
        this.count = count;
        this.powerCoefficient = powerCoefficient;
        this.priceCoefficient = priceCoefficient;
    }

    public Upgrade() {

    }

    public int getID() {
        return id;
    }

    public int getPower() {
        return power;
    }

    public int getPrice() {
        return price;
    }

    public int getCount() {
        return count;
    }

    public float getPowerCoefficient() {
        return powerCoefficient;
    }

    public float getPriceCoefficient() {
        return priceCoefficient;
    }

    public void setID(int ID) {
        id = ID;
    }

    public void setPower(int pwr) {
        power = pwr;
    }

    public void setPrice(int prc) {
        price = prc;
    }

    public void setCount(int cnt) {
        count = cnt;
    }

    public void setPowerCoefficient(float pwrC) {
        powerCoefficient = pwrC;
    }

    public void setPriceCoefficient(float pcrC) {
        priceCoefficient = pcrC;
    }
}
