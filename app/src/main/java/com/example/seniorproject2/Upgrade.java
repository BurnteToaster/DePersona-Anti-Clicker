package com.example.seniorproject2;

public class Upgrade {
    private int id;
    private int power;
    private int price;
    private int count;
    private String powerFunction;
    private String priceFunction;

    public Upgrade(int id, int power, int price, int count, String powerFunction, String priceFunction) {
        this.id = id;
        this.power = power;
        this.price = price;
        this.count = count;
        this.powerFunction = powerFunction;
        this.priceFunction = priceFunction;
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

    public String getPowerFunction() {
        return powerFunction;
    }

    public String getPriceFunction() {
        return priceFunction;
    }

    public void setID(int ID) {
    }

    public void setPower(int power) {
    }

    public void setPrice(int price) {
    }

    public void setPowerFunction(String powerFunction) {
    }

    public void setPriceFunction(String priceFunction) {
    }

    public void setCount(int count) {
    }
}
