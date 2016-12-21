package com.test.marvelapp.models;


import java.util.ArrayList;

/**
 * Created by Nicolas on 28/05/2016.
 */
public class MarvelResults {

    private int id;
    private String name;
    private String title;
    private String description;
    private MarvelThumbnail thumbnail;
    private MarvelCreators creators;
    private MarvelCharacters characters;
    private MarvelStories stories;
    private MarvelComics comics;
    private String modified;
    private int pageCount;

    private ArrayList<MarvelPrices> prices = new ArrayList<>();

    public MarvelResults() {}

    public MarvelResults(String name) {
        this.name = name;
    }
    public MarvelResults(String name, String description, MarvelThumbnail thumbnail) {
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public ArrayList<MarvelPrices> getPrices() {
        return prices;
    }

    public void setPrices(ArrayList<MarvelPrices> prices) {
        this.prices = prices;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MarvelThumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(MarvelThumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public MarvelComics getComics() {
        return comics;
    }

    public void setComics(MarvelComics comics) {
        this.comics = comics;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public MarvelCreators getCreators() {
        return creators;
    }

    public void setCreators(MarvelCreators creators) {
        this.creators = creators;
    }

    public MarvelCharacters getCharacters() {
        return characters;
    }

    public void setCharacters(MarvelCharacters characters) {
        this.characters = characters;
    }

    public MarvelStories getStories() {
        return stories;
    }

    public void setStories(MarvelStories stories) {
        this.stories = stories;
    }
}
