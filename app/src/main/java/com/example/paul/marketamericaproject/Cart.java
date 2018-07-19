package com.example.paul.marketamericaproject;

import java.util.ArrayList;

public class Cart {
    ArrayList<String> contents;

    public Cart() {
        contents = new ArrayList<>();
    }

    public Cart(ArrayList<String> c) {
        contents = c;
    }

    public ArrayList<String> getContents() {
        return contents;
    }

    public void setContents(ArrayList<String> contents) {
        this.contents = contents;
    }

    public void addItem(String item) {
        contents.add(item);
    }

    public void removeItem(int index) {
        contents.remove(index);
    }

    public String getID(int index) {
        return contents.get(index);
    }
}
