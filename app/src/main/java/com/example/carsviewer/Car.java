package com.example.carsviewer;

public class Car {
    private int id;
    private String model ;
    private String color ;
    private String decription ;
    private String image ;
    private double dpl ;


    public Car(int id, String model, String color, String decription, String image, double dpl) {
        this.id = id;
        this.model = model;
        this.color = color;
        this.decription = decription;
        this.image = image;
        this.dpl = dpl;
    }

    public Car(String model, String color, String decription, String image, double dpl) {
        this.model = model;
        this.color = color;
        this.decription = decription;
        this.image = image;
        this.dpl = dpl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getDpl() {
        return dpl;
    }

    public void setDpl(double dpl) {
        this.dpl = dpl;
    }
}
