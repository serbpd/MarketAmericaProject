package com.example.paul.marketamericaproject;

import java.io.Serializable;
import java.text.ParseException;

public class Product implements Serializable, Comparable<Product> {

    String name, brand, shortDescr, longDescr, imageURL, ID;
    Double price;

    public Product() {}

    public Product(String name, String brand, String shortDescr, String imageURL, String ID, Double price) throws ParseException {
        this.name = name;
        this.brand = brand;
        this.shortDescr = shortDescr;
        this.imageURL = imageURL;
        this.ID = ID;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getShortDescr() {
        return shortDescr;
    }

    public void setShortDescr(String shortDescr) {
        this.shortDescr = shortDescr;
    }

    public String getLongDescr() {
        return longDescr;
    }

    public void setLongDescr(String longDescr) {
        this.longDescr = longDescr;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public int compareTo( Product other) {
        return this.price.compareTo(other.price);
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", shortDescr='" + shortDescr + '\'' +
                ", longDescr='" + longDescr + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", ID='" + ID + '\'' +
                ", price=" + price +
                '}';
    }
}

