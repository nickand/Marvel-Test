package com.test.marvelapp.database;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.test.marvelapp.models.MarvelResults;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLCondition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by javiexpo on 26/7/16.
 */
public class DBManager {
    private static final String TAG = DBManager.class.getName();
    private static DBManager mInstance = null;
    private static Context mCtx;

    private int mSavedCounter = 0;
    private DBManager(Context context){
        mCtx = context;
    }

    public static synchronized DBManager getInstance(Context context){
        if(mInstance == null)
            mInstance = new DBManager(context);
        else
            mCtx = context;
        return mInstance;
    }

    public boolean hasComics(){
        List<CharactersTb> charactersList = new Select().from(CharactersTb.class).queryList();
        return charactersList.size() > 0;
    }

    public boolean hasCharactersFromTable(Class<? extends BaseModel> className, SQLCondition sqlCondition) {
        return new Select().from(className).where(sqlCondition).hasData();
    }

    public boolean saveCharacters(ArrayList<MarvelResults> comics){
        boolean saved = false;
        mSavedCounter = 0;

        for (MarvelResults item : comics) {
            CharactersTb prevRecord = recordExist(item.getId());
            if (prevRecord == null) {
                CharactersTb charactersTb = new CharactersTb();
                charactersTb.setId(item.getId());
                charactersTb.setTitle(item.getTitle());
                charactersTb.setPrice(item.getPrices().get(0).getPrice());
                charactersTb.setStories(item.getStories().getItems().get(0).getName());

                if (item.getCreators().getItems().size() > 0) {
                    StringBuilder commaSepValueBuilder = new StringBuilder();
                    String dataCreators = "";
                    for (int i = 0; i < item.getCreators().getItems().size(); i++) {
                        dataCreators = commaSepValueBuilder.append(item.getCreators().getItems().get(i).getName()).toString();

                        if ( i != item.getCreators().getItems().size()-1){
                            commaSepValueBuilder.append(", ");
                        }
                    }
                    charactersTb.setCreators(dataCreators);
                } else {
                    charactersTb.setCreators("No creators available");
                }

                if (item.getCharacters().getItems().size() > 0) {
                    StringBuilder commaSepValueBuilder = new StringBuilder();
                    String dataCharacters = "";
                    for (int i = 0; i < item.getCharacters().getItems().size(); i++) {
                        dataCharacters = commaSepValueBuilder.append(item.getCharacters().getItems().get(i).getName()).toString();

                        if ( i != item.getCreators().getItems().size()-1){
                            commaSepValueBuilder.append(", ");
                        }
                    }
                    charactersTb.setCharacters(dataCharacters);
                } else {
                    charactersTb.setCharacters("No characters available");
                }

                charactersTb.setDescription(item.getDescription());
                charactersTb.setModified(item.getModified());
                charactersTb.setPageCount(item.getPageCount());
                charactersTb.setThumbnail(item.getThumbnail().getPath() + "." + item.getThumbnail().getExtension());
                charactersTb.save();
                mSavedCounter++;
            }
        }

        if (mSavedCounter == comics.size())
            saved = true;

        return saved;
    }

    public CharactersTb recordExist(int id){
        CharactersTb record = null;
        List<CharactersTb> charactersList = new Select().from(CharactersTb.class).where(FavoritesTb_Table.id.eq(id)).queryList();
        for (int i=0; i < charactersList.size(); i++) {
            CharactersTb car = charactersList.get(i);
            Log.d(TAG, car.getTitle());
        }

        if (charactersList.size() > 0){
            record = charactersList.get(0);
        }
        return record;
    }

    public List<CharactersTb> getCharacters() {
        CharactersTb record = null;
        List<CharactersTb> charactersList = new Select().from(CharactersTb.class).queryList();
        for (int i=0; i < charactersList.size(); i++) {
            CharactersTb car = charactersList.get(i);
            Log.d(TAG, "PERSONAJES: "+car.getTitle());
            Log.d(TAG, "PRECIOS: "+car.getPrice());
            Log.d(TAG, "STORIES: "+car.getStories());
            Log.d(TAG, "CREATORS: "+car.getCreators());
        }


        if (charactersList.size() > 0){
            record = charactersList.get(0);
        }
        return charactersList;
    }

    /*public List<FavoritesTb> getFavorites(){
        List<FavoritesTb> charactersList = new Select().from(FavoritesTb.class).queryList();
        for (int i=0; i < charactersList.size(); i++) {
            FavoritesTb fav = charactersList.get(i);
            Log.d(TAG, "PERSONAJES: "+ fav.getCharFav().getName());
        }
        return charactersList;
    }*/

    public void saveAsFavorite(CharactersTb charFav){
        FavoritesTb fav = new FavoritesTb();
        fav.setId(charFav.getId());
        fav.setSelected(charFav.exists());
        fav.setCharFav(charFav);
        fav.save();
    }

    public void removeFavorite(CharactersTb charFav){
        Delete.table(FavoritesTb.class, FavoritesTb_Table.id.eq(charFav.getId()));
    }

    public List<?> getComicsFromTable(Class<? extends BaseModel> className) {
        return new Select().from(className).queryList();
    }

    public List<?> getComicsFromTableWhere(Class<? extends BaseModel> className, SQLCondition sqlCondition) {
        return new Select().from(className).where(sqlCondition).queryList();
    }
}