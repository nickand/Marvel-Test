package com.test.marvelapp.database;

import android.os.Parcel;
import android.os.Parcelable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by javiexpo on 25/7/16.
 */
@Table(database = MarvelDB.class)
public class FavoritesTb extends BaseModel implements Parcelable {

    int Favoriteid;
    String FavoriteCharFav;
    byte FavoriteIsSelected;

    @Column
    @PrimaryKey
    int id;

    @Column
    @PrimaryKey
    @ForeignKey(saveForeignKeyModel = false)
    CharactersTb charFav;

    @Column
    boolean isSelected;

    public FavoritesTb(){};

    public static final Creator<FavoritesTb> CREATOR = new Creator<FavoritesTb>() {
        @Override
        public FavoritesTb createFromParcel(Parcel in) {
            return new FavoritesTb(in);
        }

        @Override
        public FavoritesTb[] newArray(int size) {
            return new FavoritesTb[size];
        }
    };

    public CharactersTb getCharFav() {
        return charFav;
    }

    public void setCharFav(CharactersTb charFav) {
        this.charFav = charFav;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected FavoritesTb(Parcel in) {
        Favoriteid = getId();
        Favoriteid = in.readInt();
        FavoriteCharFav = charFav.getCharacters();
        FavoriteCharFav = in.readString();
        FavoriteIsSelected = (byte) (isSelected() ? 1 : 0);
        FavoriteIsSelected = in.readByte();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(charFav.getCharacters());
        parcel.writeByte((byte) (isSelected ? 1 : 0));
    }


}