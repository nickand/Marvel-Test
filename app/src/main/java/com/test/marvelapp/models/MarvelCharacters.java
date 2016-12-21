package com.test.marvelapp.models;

import java.util.ArrayList;

/**
 * Created by Nicolas on 18/12/2016.
 */
public class MarvelCharacters {
    private ArrayList<Items> items = new ArrayList<>();

    public ArrayList<Items> getItems() {
        return items;
    }

    public void setItems(ArrayList<Items> items) {
        this.items = items;
    }
}
