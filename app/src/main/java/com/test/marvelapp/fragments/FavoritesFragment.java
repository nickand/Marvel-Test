package com.test.marvelapp.fragments;

import android.content.Context;
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

import com.test.marvelapp.R;
import com.test.marvelapp.Utils.Constants;
import com.test.marvelapp.Utils.GridSpacingItemDecoration;
import com.test.marvelapp.adapters.FavoritesAdapter;
import com.test.marvelapp.database.CharactersTb;
import com.test.marvelapp.database.DBManager;
import com.test.marvelapp.database.FavoritesTb;
import com.test.marvelapp.interfaces.OnClickActivityListener;

import java.util.ArrayList;

/**
 * Created by Nicolas on 23/12/2016.
 */

public class FavoritesFragment extends Fragment {

    private static final String ARG_PARAM1 = "param";

    private static final String TAG = FavoritesFragment.class.getName();
    private RecyclerView recyclerView;
    private FavoritesAdapter adapter;
    private ArrayList<CharactersTb> mFavorites;

    private GridSpacingItemDecoration gridItemDecoration;
    private ArrayList<FavoritesTb> favs;

    private OnClickActivityListener mListener;

    private int mId = 0;
    private String mDescription = "";
    private float mPrice = 0;
    private String mDate = "";
    private String mStories = "";
    private String mCreators = "";
    private String mCharacters = "";
    private String mTitle = "";
    private String mThumb = "";
    private int mPageCount = 0;

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

    public static FavoritesFragment newInstance(Bundle args) {
        FavoritesFragment favoritesFragment = new FavoritesFragment();
        favoritesFragment.setArguments(args);
        return favoritesFragment;
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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnClickActivityListener) {
            mListener = (OnClickActivityListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnClickActivityListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        favs = (ArrayList<FavoritesTb>) DBManager.getInstance(getActivity()).getComicsFromTable(FavoritesTb.class);

        for (int i = 0; i < favs.size(); i++){
//            Log.d(TAG, favs.get(i).getCharFav().getName()+"-"+favs.get(i).getCharFav().getId());
            mFavorites.add(favs.get(i).getCharFav());
        }

        if (mFavorites != null) {
            adapter = new FavoritesAdapter(getActivity(), mFavorites, mListener);
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
