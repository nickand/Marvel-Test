package com.test.marvelapp.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by javiexpo on 25/7/16.
 */
@Table(database = MarvelDB.class)
public class ThumbnailTb extends BaseModel {

    @Column
    @PrimaryKey
    private int id;

    @Column
    @PrimaryKey
    private String path;

    @Column
    private String extension;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

}
