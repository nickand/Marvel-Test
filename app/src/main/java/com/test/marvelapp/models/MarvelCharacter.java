package com.test.marvelapp.models;

import java.util.ArrayList;

/**
 * Created by Nicolas on 28/05/2016.
 */
public class MarvelCharacter {

    private ArrayList<MarvelResults> results = new ArrayList<>();

    public ArrayList<MarvelResults> getResults() {
        return results;
    }

    public void setResults(ArrayList<MarvelResults> results) {
        this.results = results;
    }
}
