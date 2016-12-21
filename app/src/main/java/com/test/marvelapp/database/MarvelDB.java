package com.test.marvelapp.database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by javiexpo on 25/7/16.
 */
//Aquí defines la instancia de la BD. Te permite controlar y hacer migraciones en la BD.
@Database(name = MarvelDB.NAME, version = MarvelDB.VERSION)
public class MarvelDB {
    public static final String NAME = "MarvelDB";

    public static final int VERSION = 1;
}
