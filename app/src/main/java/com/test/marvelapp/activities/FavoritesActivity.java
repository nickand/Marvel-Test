package com.test.marvelapp.activities;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;

import com.test.marvelapp.R;
import com.test.marvelapp.Utils.GridSpacingItemDecoration;
import com.test.marvelapp.adapters.MarvelAdapter;
import com.test.marvelapp.database.CharactersTb;
import com.test.marvelapp.database.DBManager;
import com.test.marvelapp.database.FavoritesTb;

import java.util.ArrayList;
import java.util.List;


public class FavoritesActivity extends AppCompatActivity {

    private static final String CLASS_TAG = FavoritesActivity.class.getName();
    private RecyclerView recyclerView;
    private MarvelAdapter adapter;
    private List<CharactersTb> mFavorites;

    private GridSpacingItemDecoration gridItemDecoration;
    private List<FavoritesTb> favs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_favorites);

        gridItemDecoration = new GridSpacingItemDecoration(this, 2, dpToPx(1), true);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(gridItemDecoration);

        loadFavorites();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(CLASS_TAG, "ON RESUME");
        loadFavorites();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(CLASS_TAG, "ON PAUSE");
    }

    public void loadFavorites(){
        mFavorites = new ArrayList<CharactersTb>();
        favs = (List<FavoritesTb>) DBManager.getInstance(this).getComicsFromTable(FavoritesTb.class);

        for (int i = 0; i < favs.size(); i++){
            Log.d(CLASS_TAG, favs.get(i).getCharFav().getName()+"-"+favs.get(i).getCharFav().getId());
            mFavorites.add(favs.get(i).getCharFav());
        }

        if (mFavorites != null) {
//            adapter = new MarvelAdapter(FavoritesActivity.this, mFavorites);
//            recyclerView.setAdapter(adapter);
        } else {
            Log.d(CLASS_TAG, "ADAPTER FAVORITOS VACIO");
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Converting dp to pixel
     */
    public int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
