package com.test.marvelapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.marvelapp.R;
import com.test.marvelapp.database.CharactersTb;
import com.test.marvelapp.database.CharactersTb_Table;
import com.test.marvelapp.database.DBManager;
import com.test.marvelapp.database.FavoritesTb;
import com.test.marvelapp.database.FavoritesTb_Table;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Nicolas on 29/05/2016.
 */
public class DetailsActivity extends AppCompatActivity {

    private static final String EXTRA_ID = "com.marvelapp.mId";
    private static final String EXTRA_DESCRIPTION = "com.marvelapp.mDescription";
    private static final String EXTRA_PRICE = "com.marvelapp.mPrice";
    private static final String EXTRA_DATE = "com.marvelapp.mDate";
    private static final String EXTRA_PAGE_COUNT = "com.marvelapp.mPageCount";
    private static final String EXTRA_STORIES = "com.marvelapp.mStories";
    private static final String EXTRA_CREATORS = "com.marvelapp.mCreators";
    private static final String EXTRA_CHARACTERS = "com.marvelapp.mCharacters";
    private static final String CLASS_TAG = DetailsActivity.class.getName();
    private static final String EXTRA_TITLE = "com.marvelapp.mTitle";
    private static final String EXTRA_THUMBNAIL = "com.marvelapp.mThumb";

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

    /**
     * Inicia una nueva instancia de la actividad
     *
     * @param activity Contexto desde donde se lanzará
     * @param results  Item a procesar
     */
    public static void createInstance(Activity activity, CharactersTb results) {
        Intent intent = getLaunchIntent(activity, results);
        activity.startActivity(intent);
    }

    /**
     * Construye un Intent a partir del contexto y la actividad
     * de detalle.
     *
     * @param context   Contexto donde se inicia
     * @param character Identificador del personaje
     * @return Intent listo para usar
     */
    public static Intent getLaunchIntent(Context context, CharactersTb character) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(EXTRA_ID, character.getId());
        intent.putExtra(EXTRA_TITLE, character.getTitle());
        intent.putExtra(EXTRA_THUMBNAIL, character.getThumbnail());
        intent.putExtra(EXTRA_DESCRIPTION, character.getDescription());
        intent.putExtra(EXTRA_PRICE, character.getPrice());
        intent.putExtra(EXTRA_DATE, character.getModified());
        intent.putExtra(EXTRA_PAGE_COUNT, character.getPageCount());
        intent.putExtra(EXTRA_STORIES, character.getStories());
        intent.putExtra(EXTRA_CREATORS, character.getCreators());
        intent.putExtra(EXTRA_CHARACTERS, character.getCharacters());
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setToolbar();// Añadir action bar

        if (getSupportActionBar() != null) // Habilitar up button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Setear escucha al FAB
        fab = (FloatingActionButton) findViewById(R.id.fab);

        dbManager = DBManager.getInstance(getApplicationContext());

        mCharacter = new CharactersTb();

        Intent i = getIntent();
        mId = i.getIntExtra(EXTRA_ID, 0);
        mTitle = i.getStringExtra(EXTRA_TITLE);
        mThumb = i.getStringExtra(EXTRA_THUMBNAIL);
        mDescription = i.getStringExtra(EXTRA_DESCRIPTION);
        mPrice = i.getFloatExtra(EXTRA_PRICE, 0);
        mDate = i.getStringExtra(EXTRA_DATE);
        mPageCount = i.getIntExtra(EXTRA_PAGE_COUNT, 0);
        mStories = i.getStringExtra(EXTRA_STORIES);
        mCreators = i.getStringExtra(EXTRA_CREATORS);
        mCharacters = i.getStringExtra(EXTRA_CHARACTERS);

        initAndValidateRetrofitData();

        getComicById(mId);
        mFav = new FavoritesTb();
        mListfavs = (List<FavoritesTb>) DBManager.getInstance(this).getComicsFromTableWhere(FavoritesTb.class, FavoritesTb_Table.id.eq(mId));

        for (int j = 0; j < mListfavs.size(); j++) {
            if (mListfavs.get(j).getCharFav().getId() == mCharacter.getId()) {
                Log.d(CLASS_TAG, mId + " - " + mListfavs.get(j).getCharFav().getId());
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
                                addCharacterToFavorite();
                                mFav.setSelected(true);

                            } else {
                                fab.setImageResource(R.mipmap.ic_star_outline_white_24dp);
                                removeCharacterFromFavorite();
                                mFav.setSelected(false);
                            }
                        }
                    }
            );
        }
    }

    private void initAndValidateRetrofitData() {
        TextView text_description = (TextView) findViewById(R.id.tv_description);
        TextView tvPrice = (TextView) findViewById(R.id.tvPrice);
        TextView tvDate = (TextView) findViewById(R.id.tvDate);
        TextView tvPageCount = (TextView) findViewById(R.id.tvPageCount);
        TextView tvStories = (TextView) findViewById(R.id.tvStories);
        TextView tvCreators = (TextView) findViewById(R.id.tvCreators);
        TextView tvCharacters = (TextView) findViewById(R.id.tvCharacters);

        CollapsingToolbarLayout collapser =
                (CollapsingToolbarLayout) findViewById(R.id.collapser);

        try {

            if (mTitle != null && !mTitle.isEmpty()) {
                collapser.setTitle(mTitle); // Cambiar título
            } else {
                collapser.setTitle("Unknown"); // Cambiar título
            }

            if (mThumb != null && !mThumb.isEmpty()) {
                loadImageParallax(mThumb); // Cambiar título
            } else {
                loadImageParallax(getResources().getResourceName(R.mipmap.ic_launcher)); // Cambiar título
            }

            // Cargar Imagen


        } catch (Exception e) {
            Log.e(CLASS_TAG, "ERROR: " + e.getMessage());
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
        List<CharactersTb> heros
                = (List<CharactersTb>) DBManager.getInstance(this).getComicsFromTableWhere(CharactersTb.class, CharactersTb_Table.id.eq(characterId));

        if (heros.size() > 0)
            mCharacter = heros.get(0);

    }

    public void addCharacterToFavorite() {
        Log.i(CLASS_TAG, "Clicked!");
        showSnackBar("Agregado a Favoritos");
        dbManager.saveAsFavorite(mCharacter);
    }

    public void removeCharacterFromFavorite() {
        Log.i(CLASS_TAG, "Clicked!");
        showSnackBar("Eliminado de Favoritos");
        DBManager dbManager = DBManager.getInstance(this);
        dbManager.removeFavorite(mCharacter);
    }

    private void setToolbar() {
        // Añadir la Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * Se carga una imagen aleatoria para el detalle
     */
    private void loadImageParallax(String id) {
        ImageView image = (ImageView) findViewById(R.id.image_paralax);
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
    private void showSnackBar(String msg) {
        Snackbar
                .make(findViewById(R.id.coordinator), msg, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                showSnackBar("Se abren los ajustes");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}