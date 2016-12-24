package com.test.marvelapp.database;

import android.os.Parcel;
import android.os.Parcelable;

import com.test.marvelapp.models.MarvelPrices;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.ArrayList;

/**
 * Created by javiexpo on 25/7/16.
 */
@Table(database = MarvelDB.class)
public class CharactersTb extends BaseModel implements Parcelable {
    private String comicTitle;
    private String comicDescription;
    private float comicPrice;
    private int comicPages;
    private String comicDate;
    private String comicImage;
    private String comicSeries;
    private String comicCreators;
    private String comicCharacters;

    @Column
    @PrimaryKey
    int id;

    @Column
    String name;

    @Column
    String title;

    @Column
    String description;

    @Column
    String modified;

    @Column
    String thumbnail;

    @Column
    String stories;

    @Column
    String creators;

    @Column
    String characters;

    @Column
    String fullName;

    @Column
    String imageProfile;

    @Column
    int pageCount;

    @Column
    float price;

    public CharactersTb(){}

    public static final Creator<CharactersTb> CREATOR = new Creator<CharactersTb>() {
        @Override
        public CharactersTb createFromParcel(Parcel in) {
            return new CharactersTb(in);
        }

        @Override
        public CharactersTb[] newArray(int size) {
            return new CharactersTb[size];
        }
    };

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getStories() {
        return stories;
    }

    public void setStories(String stories) {
        this.stories = stories;
    }

    public String getCreators() {
        return creators;
    }

    public void setCreators(String creators) {
        this.creators = creators;
    }

    public String getCharacters() {
        return characters;
    }

    public void setCharacters(String characters) {
        this.characters = characters;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public CharactersTb(Parcel in) {
        comicTitle = getTitle();
        comicTitle = in.readString();
        comicDescription = getDescription();
        comicDescription = in.readString();
        comicImage = getThumbnail();
        comicImage = in.readString();
        comicDate = getModified();
        comicDate = in.readString();
        comicPrice = getPrice();
        comicPrice = in.readFloat();
        comicPages = getPageCount();
        comicPages = in.readInt();
        comicSeries = getStories();
        comicSeries = in.readString();
        comicCreators = getCreators();
        comicCreators = in.readString();
        comicCharacters = in.readString();
        comicCharacters = in.readString();
//        in.readTypedList(appImage, AppImage.CREATOR);


    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(modified);
        parcel.writeString(thumbnail);
        parcel.writeString(stories);
        parcel.writeString(creators);
        parcel.writeString(characters);
        parcel.writeString(fullName);
        parcel.writeString(imageProfile);
        parcel.writeInt(pageCount);
        parcel.writeFloat(price);
    }
}
