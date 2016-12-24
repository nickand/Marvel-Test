package com.test.marvelapp.fragments;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.test.marvelapp.R;
import com.test.marvelapp.Utils.Constants;
import com.test.marvelapp.database.CharactersTb;
import com.test.marvelapp.database.CharactersTb_Table;
import com.test.marvelapp.database.DBManager;
import com.test.marvelapp.database.FavoritesTb;
import com.test.marvelapp.database.FavoritesTb_Table;

import java.util.List;

public class DetailFragment extends Fragment {

    private static final String TAG = DetailFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "param";
    private CharactersTb mCharacter;
    private FavoritesTb mFav;
    private FloatingActionButton fab;
    private DBManager dbManager;
    private List<FavoritesTb> mListfavs;
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

    public DetailFragment() {
    }

    public static DetailFragment newInstance() {
        return new DetailFragment();
    }

    public static DetailFragment newInstance(CharactersTb param) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, param);
        fragment.setArguments(args);
        return fragment;
    }

    public static DetailFragment newInstance(Bundle args) {
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_detail, container, false);

//        setToolbar();// Añadir action bar
//
//        if (getSupportActionBar() != null) // Habilitar up button
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Setear escucha al FAB
        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        dbManager = DBManager.getInstance(getActivity());

        mCharacter = new CharactersTb();

        Bundle bundle = getArguments();
        mId = bundle.getInt(Constants.EXTRA_ID);
        mTitle = bundle.getString(Constants.EXTRA_TITLE);
        mThumb = bundle.getString(Constants.EXTRA_THUMBNAIL);
        mDescription = bundle.getString(Constants.EXTRA_DESCRIPTION);
        mPrice = bundle.getFloat(Constants.EXTRA_PRICE, 0);
        mDate = bundle.getString(Constants.EXTRA_DATE);
        mPageCount = bundle.getInt(Constants.EXTRA_PAGE_COUNT, 0);
        mStories = bundle.getString(Constants.EXTRA_STORIES);
        mCreators = bundle.getString(Constants.EXTRA_CREATORS);
        mCharacters = bundle.getString(Constants.EXTRA_CHARACTERS);

        initAndValidateRetrofitData(view);

        getComicById(mId);
        mFav = new FavoritesTb();
        mListfavs = (List<FavoritesTb>) DBManager.getInstance(getActivity())
                .getComicsFromTableWhere(FavoritesTb.class, FavoritesTb_Table.id.eq(mId));

        for (int j = 0; j < mListfavs.size(); j++) {
            if (mListfavs.get(j).getCharFav().getId() == mCharacter.getId()) {
                Log.d(TAG, mId + " - " + mListfavs.get(j).getCharFav().getId());
                fab.setImageResource(R.mipmap.ic_star_white_24dp);
                mFav.setSelected(true);
            } else {
                fab.setImageResource(R.mipmap.ic_star_outline_white_24dp);
                mFav.setSelected(false);
            }
        }

        if (fab != null) {
            fab.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (!mFav.isSelected()) {
                                fab.setImageResource(R.mipmap.ic_star_white_24dp);
                                addCharacterToFavorite(view);
                                mFav.setSelected(true);

                            } else {
                                fab.setImageResource(R.mipmap.ic_star_outline_white_24dp);
                                removeCharacterFromFavorite(view);
                                mFav.setSelected(false);
                            }
                        }
                    }
            );
        }

        return view;
    }

    private void initAndValidateRetrofitData(View view) {
        TextView text_description = (TextView) view.findViewById(R.id.tv_description);
        TextView tvPrice = (TextView) view.findViewById(R.id.tvPrice);
        TextView tvDate = (TextView) view.findViewById(R.id.tvDate);
        TextView tvPageCount = (TextView) view.findViewById(R.id.tvPageCount);
        TextView tvStories = (TextView) view.findViewById(R.id.tvStories);
        TextView tvCreators = (TextView) view.findViewById(R.id.tvCreators);
        TextView tvCharacters = (TextView) view.findViewById(R.id.tvCharacters);

        CollapsingToolbarLayout collapser =
                (CollapsingToolbarLayout) view.findViewById(R.id.collapser);

        try {

            if (mTitle != null && !mTitle.isEmpty()) {
                collapser.setTitle(mTitle); // Cambiar título
            } else {
                collapser.setTitle("Unknown"); // Cambiar título
            }

            if (mThumb != null && !mThumb.isEmpty()) {
                loadImageParallax(mThumb, view); // Cambiar título
            } else {
                loadImageParallax(getResources().getResourceName(R.mipmap.ic_launcher), view); // Cambiar título
            }

            // Cargar Imagen


        } catch (Exception e) {
            Log.e(TAG, "ERROR: " + e.getMessage());
        }

        if (mDescription != null && !mDescription.isEmpty())
            text_description.setText(mDescription);
        else
            text_description.setText("No description available");

        if (mStories != null && !mStories.isEmpty())
            tvStories.setText(mStories);
        else
            tvStories.setText("No stories available");

        if (mCreators != null && !mCreators.isEmpty())
            tvCreators.setText(mCreators);
        else
            tvCreators.setText("No creators available");

        if (mCharacters != null && !mCharacters.isEmpty())
            tvCharacters.setText(mCharacters);
        else
            tvCharacters.setText("No characters available");

        if (mPrice != 0 && mPrice > 0)
            tvPrice.setText(String.valueOf(mPrice).concat("$"));
        else
            tvPrice.setText("Not available");

        if (mDate != null && !mDate.isEmpty()) {
            tvDate.setText(dateFormat(mDate));
        } else {
            tvDate.setText("Not available");
        }

        if (mPageCount != 0 && mPageCount > 0)
            tvPageCount.setText(String.valueOf(mPageCount).concat(" pages"));
        else
            tvPageCount.setText("Not available");
    }

    private String dateFormat(String mDate) {
        String strAbbreviate = mDate.substring(0, 10);
        String year = strAbbreviate.substring(0, 4);
        String month = strAbbreviate.substring(5, 7);
        String day = strAbbreviate.substring(8, 10);

        String dateFormat = day.concat("/".concat(month)).concat("/".concat(year));

        return dateFormat;
    }

    private void getComicById(int characterId) {
        List<CharactersTb> comics
                = (List<CharactersTb>) DBManager.getInstance(getActivity())
                .getComicsFromTableWhere(CharactersTb.class, CharactersTb_Table.id.eq(characterId));

        if (comics.size() > 0)
            mCharacter = comics.get(0);

    }

    public void addCharacterToFavorite(View view) {
        Log.i(TAG, "Clicked!");
        showSnackBar("Agregado a Favoritos", view);
        dbManager.saveAsFavorite(mCharacter);
    }

    public void removeCharacterFromFavorite(View view) {
        Log.i(TAG, "Clicked!");
        showSnackBar("Eliminado de Favoritos", view);
        DBManager dbManager = DBManager.getInstance(getActivity());
        dbManager.removeFavorite(mCharacter);
    }

    /**
     * Se carga una imagen aleatoria para el detalle
     */
    private void loadImageParallax(String id, View view) {
        ImageView image = (ImageView) view.findViewById(R.id.image_paralax);
        // Usando Glide para la carga asíncrona
        Glide.with(this)
                .load(id)
                .centerCrop()
                .into(image);
    }

    /**
     * Proyecta una {@link Snackbar} con el string usado
     *
     * @param msg Mensaje
     */
    private void showSnackBar(String msg, View view) {
        Snackbar
                .make(view.findViewById(R.id.coordinator), msg, Snackbar.LENGTH_LONG)
                .show();
    }
}
