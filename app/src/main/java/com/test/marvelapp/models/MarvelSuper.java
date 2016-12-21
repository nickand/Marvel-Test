package com.test.marvelapp.models;

/**
 * Created by Nicolas on 29/05/2016.
 */
public class MarvelSuper {

    private int code;
    private String status;
    private String copyright;
    private MarvelCharacter data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public MarvelCharacter getData() {
        return data;
    }

    public void setData(MarvelCharacter data) {
        this.data = data;
    }
}
