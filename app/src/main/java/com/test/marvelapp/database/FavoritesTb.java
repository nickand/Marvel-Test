package com.test.marvelapp.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by javiexpo on 25/7/16.
 */
@Table(database = MarvelDB.class)
public class FavoritesTb extends BaseModel {
    @Column
    @PrimaryKey
    int id;

    @Column
    @PrimaryKey
    @ForeignKey(saveForeignKeyModel = false)
    CharactersTb charFav;


    @Column
    boolean isSelected;

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
}