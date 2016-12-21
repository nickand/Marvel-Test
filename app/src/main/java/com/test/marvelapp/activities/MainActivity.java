package com.test.marvelapp.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.test.marvelapp.R;
import com.test.marvelapp.Utils.Constants;
import com.test.marvelapp.Utils.GridSpacingItemDecoration;
import com.test.marvelapp.adapters.MarvelAdapter;
import com.test.marvelapp.adapters.SearchableAdapter;
import com.test.marvelapp.database.CharactersTb;
import com.test.marvelapp.database.DBManager;
import com.test.marvelapp.services.DataService;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.http.HTTP;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {
    private static final String CLASS_TAG = MainActivity.class.getName();

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    private List<CharactersTb> characters;
    private MarvelAdapter adapter;
    private GridSpacingItemDecoration gridItemDecoration;

    private int mProgressStatus;
    private EditText editTxt;
    private SearchableAdapter mPlanetAdapter;
    private String mName;
    private String mImage;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        startLoading();
        initCollapsingToolbar();

        mProgressBar.setVisibility(View.VISIBLE);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mName = extras.getString("fullName");
            mImage = extras.getString("imageProfile");



//            intent.putExtra("fullName", mName);
//            intent.putExtra("imageProfile", mImage);

            Log.d(CLASS_TAG, "NOMBRE USUARIO= " + mName);
            Log.d(CLASS_TAG, "IMAGEN USUARIO= " + mImage);
        }

        characters = new ArrayList<CharactersTb>();

        gridItemDecoration = new GridSpacingItemDecoration(this, 2, dpToPx(1), true);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(gridItemDecoration);

        try {
            Glide.with(this).load(R.drawable.cover_marvel).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startLoading() {
        //Nos suscribimos para recibir BROADCAST_ACTION
        // Filtramos la acción BROADCAST_ACTION
        IntentFilter mStatusIntentFilter = new IntentFilter(Constants.BROADCAST_ACTION);
        //Instanciamos al receiver
        ResponseReceiver mResponseReceiver = new ResponseReceiver();
        // Registramos el receiver y el filter
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mResponseReceiver, mStatusIntentFilter);

        //Iniciamos el status del ProgressBar a 10%
        mProgressBar.setProgress(10);

        //Instanciamos un Intent para disparar el Servicio DataService
        Intent mServiceIntent = new Intent(getApplicationContext(), DataService.class);
        //Indicamos los datos que se necesitan pasar al Servicio
        mServiceIntent.setData(Uri.parse(Constants.SERVCMD_GET_COMICS));
        //Mandamos al servicio a ejecutar la acción SERVCMD_GET_CARACTERS
        startService(mServiceIntent);
    }

    private class ResponseReceiver extends BroadcastReceiver {
        // Prevenimos la instanciación
        private ResponseReceiver() {
        }

        // Este método es llamado cuando el BroadcastReceiver obtiene un Intent para el cual
        // ha sido registrado recibir
        public void onReceive(Context context, Intent intent) {
            Log.d(CLASS_TAG, "ResponseReceiver onReceive");
            switch (intent.getAction()) {
                case Constants.BROADCAST_ACTION:
                    mProgressStatus = intent.getIntExtra(Constants.EXTRA_DATA_STATUS, 10);
                    mProgressBar.setProgress(mProgressStatus);
                    if (mProgressStatus == 100) {
                        Log.d(CLASS_TAG, getString(R.string.loading_complete));
                        mProgressBar.setVisibility(View.GONE);

                        characters = DBManager.getInstance(getApplicationContext()).getCharacters();

                        for (int i = 0; i < characters.size(); i++) {
                            Log.d(CLASS_TAG, "COMICS PRECIO= " + characters.get(i).getPrice());
                        }

                        adapter = new MarvelAdapter(MainActivity.this, characters);
                        recyclerView.setAdapter(adapter);
                    }


                    break;
                default:
                    break;
            }

        }
    }

//    Intent sendIntent = new Intent();
//    sendIntent.setAction(Intent.ACTION_SEND);
//    sendIntent.putExtra(Intent.EXTRA_TEXT, textMessage);
//    sendIntent.setType(HTTP.PLAIN_TEXT_TYPE); // "text/plain" MIME type
//    startActivity(sendIntent);

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_profile) {
            CharactersTb charactersTb = new CharactersTb();
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("fullName", mName);
            intent.putExtra("imageProfile", mImage);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);


            return true;
        }

        if (id == R.id.action_search) {
            getLaunchIntent(this, ToolbarSearchActivity.class);
            return true;
        }

        if (id == R.id.action_favorite) {
            getLaunchIntent(this, FavoritesActivity.class);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Construye un Intent a partir del contexto y la actividad
     * de detalle.
     *
     * @param context Contexto donde se inicia
     * @return Intent listo para usar
     */
    public void getLaunchIntent(Context context, Class<?> classname) {
        Intent intent = new Intent(context, classname);
        startActivity(intent);
    }

    /**
     * Converting dp to pixel
     */
    public int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}