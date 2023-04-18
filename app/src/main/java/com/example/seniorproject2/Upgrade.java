package com.example.seniorproject2;

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
    }

    public void setPower(int power) {
    }

    public void setPrice(int price) {
    }

    public void setCount(int count) {
    }

    public void setPowerFunction(float powerFunction) {
    }

    public void setPriceFunction(float priceFunction) {
    }
}
