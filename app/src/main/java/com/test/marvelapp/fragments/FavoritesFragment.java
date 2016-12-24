package com.test.marvelapp.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.test.marvelapp.R;
import com.test.marvelapp.Utils.GridSpacingItemDecoration;
import com.test.marvelapp.activities.FavoritesActivity;
import com.test.marvelapp.adapters.MarvelAdapter;
import com.test.marvelapp.database.CharactersTb;
import com.test.marvelapp.database.DBManager;
import com.test.marvelapp.database.FavoritesTb;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Nicolas on 23/12/2016.
 */

public class FavoritesFragment extends Fragment {

    private static final String ARG_PARAM1 = "param";
    private static FavoritesFragment fragment;

    private static final String TAG = FavoritesFragment.class.getName();
    private RecyclerView recyclerView;
    private MarvelAdapter adapter;
    private List<CharactersTb> mFavorites;

    private GridSpacingItemDecoration gridItemDecoration;
    private List<FavoritesTb> favs;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    public static FavoritesFragment newInstance(FavoritesTb e) {
        FavoritesFragment fragment = new FavoritesFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, e);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_favorites);

        gridItemDecoration = new GridSpacingItemDecoration(getActivity(), 2, dpToPx(1), true);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(gridItemDecoration);

        loadFavorites();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "ON RESUME");
        loadFavorites();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "ON PAUSE");
    }

    public void loadFavorites(){
        mFavorites = new ArrayList<CharactersTb>();
        favs = (List<FavoritesTb>) DBManager.getInstance(getActivity()).getComicsFromTable(FavoritesTb.class);

        for (int i = 0; i < favs.size(); i++){
//            Log.d(TAG, favs.get(i).getCharFav().getName()+"-"+favs.get(i).getCharFav().getId());
            mFavorites.add(favs.get(i).getCharFav());
        }

        if (mFavorites != null) {
            adapter = new MarvelAdapter(getActivity(), mFavorites);
            recyclerView.setAdapter(adapter);
        } else {
            Log.d(TAG, "ADAPTER FAVORITOS VACIO");
        }

    }

    @Override
    public void onDestroy() {
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
